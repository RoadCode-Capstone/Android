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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val email: String = "",
    val code: String = "",
    val password: String = "",
    val nickname: String = "",
    val codeTimer: Int = 0,
    val verifiedEmail: String = "",
    val isVerifyEmail: Boolean = false,
    val isVerifyNickname: Boolean = false
)

enum class RegisterField { EMAIL, CODE, PASSWORD, NICKNAME }

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    companion object {
        private const val TAG = "RegisterViewModel"
    }

    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast = _toast.asSharedFlow()
    private val _navigateBack = MutableStateFlow(false)
    val navigateBack = _navigateBack.asStateFlow()

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()

    fun init() {
        _registerUiState.value = RegisterUiState()
    }

    /* 뒤로 가기 처리 함수 */
    fun setFalseNavigateBack() {
        _navigateBack.value = false
    }

    /* 입력 필드 값 변경 함수 */
    fun updateInput(field: RegisterField, value: String) {
        val filtered = value.replace(" ", "")

        _registerUiState.update { state ->
            when (field) {
                RegisterField.EMAIL ->      state.copy(email = filtered, isVerifyEmail = false)
                RegisterField.CODE ->       state.copy(code = filtered)
                RegisterField.PASSWORD ->   state.copy(password = filtered)
                RegisterField.NICKNAME ->   state.copy(nickname = value)
            }
        }
    }

    /* 5분 타이머 동작 함수 */
    private fun startTimer(seconds: Int = 300) {
        viewModelScope.launch {
            _registerUiState.update { it.copy(codeTimer = seconds) }

            while (_registerUiState.value.codeTimer > 0) {
                delay(1000)
                _registerUiState.update { state ->
                    state.copy(codeTimer = state.codeTimer - 1)
                }
            }
        }
    }

    /* 이메일 중복 체크 함수 */
    private suspend fun checkDuplicatedEmail(): Boolean {
        val result = repository.checkDuplicatedEmail(registerUiState.value.email).first()

        return result.fold(
            onSuccess = { body ->
                if (body.code == "SUCCESS") {
                    Log.d(TAG, "이메일 중복 체크 성공\n중복 여부: ${body.data!!.duplicated}")
                    body.data.duplicated
                } else {
                    Log.d(TAG, "이메일 중복 체크 실패: ${body.code}")
                    true
                }
            },
            onFailure = { e ->
                e.printStackTrace()
                true
            }
        )
    }

    /* 이메일로 인증코드 전송 함수 */
    fun verifyEmailRegister() {
        viewModelScope.launch {
            val email = registerUiState.value.email.trim()
            // 이메일 중복 체크
            val duplicated = checkDuplicatedEmail()
            if (duplicated) {
                _toast.emit("이미 사용중인 이메일입니다.")
                return@launch
            }

            // 인증코드 발송
            val request = UserDTO.VerifyEmailRequest(email = email)

            repository.verifyEmailRegister(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _registerUiState.update { it.copy(verifiedEmail = email) }
                                startTimer()
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "인증코드 발송 성공")
                            }
                            "E004" -> { // 이미 가입된 메일로 인증 요청
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "인증코드 발송 실패: 이미 가입된 메일로 인증 요청")
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
        }
    }


    /* 인증코드 확인 함수 */
    fun verifyCode() {
        val request = UserDTO.VerifyCodeRequest(email = registerUiState.value.verifiedEmail, verificationCode = registerUiState.value.code)

        viewModelScope.launch {
            repository.verifyCode(request).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _registerUiState.update { it.copy(isVerifyEmail = true) }

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

    /* 닉네임 중복 확인 함수 */
    fun checkDuplicatedNickname() {
        viewModelScope.launch {
            repository.checkDuplicatedNickname(registerUiState.value.nickname).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val duplicated = body.data!!.duplicated
                                _registerUiState.update { it.copy(isVerifyNickname = true) }

                                Log.d(TAG, "닉네임 중복 체크 성공: ${duplicated}")
                            }
                            else -> Log.d(TAG, "닉네임 중복 체크 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }

    /* 회원가입 함수 */
    fun register() {
        val request = UserDTO.EmailRegisterRequest(
            email = registerUiState.value.verifiedEmail,
            password = registerUiState.value.password,
            nickname = registerUiState.value.nickname
        )

        viewModelScope.launch {
            repository.emailRegister(request).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _navigateBack.value = true  // 뒤로가기
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원가입 성공")
                            }
                            "E004" -> { // 이메일 중복
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원가입 실패: 이메일 중복")
                            }
                            "E005" -> { // 닉네임 중복
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원가입 실패: 닉네임 중복")
                            }
                            "E012" -> { // 이메일 인증 안함
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원가입 실패: 이메일 인증 안함")
                            }
                            "ERROR" -> { // 이메일 or 비밀번호 or 닉네임 입력X
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원가입 실패: ${body.message}")
                            }
                            else -> Log.d(TAG, "회원가입 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }
}