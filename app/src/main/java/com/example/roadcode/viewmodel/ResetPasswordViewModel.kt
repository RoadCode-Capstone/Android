package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.UserDTO
import com.example.roadcode.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ResetPasswordUiState(
    val email: String = "",
    val code: String = "",
    val password: String = "",
    val codeTimer: Int = 0,
    val verifiedEmail: String = "",
    val isVerifyEmail: Boolean = false,
    val verifyPasswordInput: String = "",   // 비밀번호 확인
    val isSame: Boolean = true              // 새로운 비밀번호와 비밀번호 확인 일치 여부
)

enum class ResetPasswordField { EMAIL, CODE, PASSWORD, VERIFY }

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    companion object {
        private const val TAG = "ResetPasswordViewModel"
    }

    private val _resetPasswordUiState = MutableStateFlow(ResetPasswordUiState())
    val resetPasswordUiState = _resetPasswordUiState.asStateFlow()

    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast = _toast.asSharedFlow()
    private val _navigateBack = MutableStateFlow(false)
    val navigateBack = _navigateBack.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    /* 뒤로 가기 처리 함수 */
    fun setFalseNavigateBack() {
        _navigateBack.value = false
    }

    /* 입력 필드 값 변경 함수 */
    fun updateInput(field: ResetPasswordField, value: String) {
        val filtered = value.replace(" ", "")

        _resetPasswordUiState.update { state ->
            when (field) {
                ResetPasswordField.EMAIL ->      state.copy(email = filtered, isVerifyEmail = false)
                ResetPasswordField.CODE ->       state.copy(code = filtered)
                ResetPasswordField.PASSWORD ->   state.copy(password = filtered, isSame = state.verifyPasswordInput == filtered)
                ResetPasswordField.VERIFY ->     state.copy(verifyPasswordInput = value, isSame = state.password == value)
            }
        }
    }

    /* 5분 타이머 동작 함수 */
    private fun startTimer(seconds: Int = 300) {
        viewModelScope.launch {
            _resetPasswordUiState.update { it.copy(codeTimer = seconds) }

            while (_resetPasswordUiState.value.codeTimer > 0) {
                delay(1000)
                _resetPasswordUiState.update { state ->
                    state.copy(codeTimer = state.codeTimer - 1)
                }
            }
        }
    }

    /* 이메일로 인증코드 전송 함수 */
    fun verifyEmailRegister() {
        viewModelScope.launch {
            _isLoading.value = true

            val request = UserDTO.VerifyEmailRequest(email = resetPasswordUiState.value.email.trim())

            repository.verifyEmailPassword(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _resetPasswordUiState.update { it.copy(verifiedEmail = resetPasswordUiState.value.email.trim(), isVerifyEmail = false) }
                                startTimer()
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "인증코드 발송 성공")
                            }
                            "E001" -> { // 가입된 사용자 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "인증코드 발송 실패: 가입된 사용자 없음")
                            }
                            "E009" -> { // 메일 전송 실패
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "인증코드 발송 실패: 메일 전송 실패")
                            }
                            "ERROR" -> { // 이메일 입력X
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "인증코드 발송 실패: ${body.message}")
                            }
                            else -> Log.d(TAG, "인증코드 발송 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
            _isLoading.value = false
        }
    }

    /* 인증코드 확인 함수 */
    fun verifyCode() {
        val request = UserDTO.VerifyCodeRequest(email = resetPasswordUiState.value.verifiedEmail, verificationCode = resetPasswordUiState.value.code)

        viewModelScope.launch {
            repository.verifyCode(request).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _resetPasswordUiState.update { it.copy(isVerifyEmail = true) }

                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "인증코드 확인 성공: ${body.message}")
                            }
                            "E010" -> { // 보안(토큰 탈취) 관련 에러
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "인증코드 확인 실패: ${body.message}")
                            }
                            "ERROR" -> { // 이메일 or 인증코드 입력X
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "인증코드 확인 실패: ${body.message}")
                            }
                            else -> Log.d(TAG, "인증코드 확인 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 비밀번호 재설정 함수 */
    fun resetPassword() {
        val request = UserDTO.ResetPasswordRequest(email = resetPasswordUiState.value.email, newPassword = resetPasswordUiState.value.password)

        viewModelScope.launch {
            repository.resetPassword(request).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _navigateBack.value = true
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "비밀번호 재설정 성공: ${body.message}")
                            }
                            "E011" -> { // 보안(토큰 탈취) 관련 에러
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "비밀번호 재설정 실패: ${body.message}")
                            }
                            "ERROR" -> { // 이메일 or 새로운 비밀번호 입력X
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "비밀번호 재설정 실패: ${body.message}")
                            }
                        }
                    }
            }
        }
    }
}