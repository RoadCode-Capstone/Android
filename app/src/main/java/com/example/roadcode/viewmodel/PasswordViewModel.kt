package com.example.roadcode.viewmodel

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.R
import com.example.roadcode.data.model.UserDTO
import com.example.roadcode.data.repository.UserRepository
import com.example.roadcode.ui.theme.PrimaryColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PasswordUiState(
    val currentPasswordInput: String = "",  // 현재 비밀번호
    val newPasswordInput: String = "",      // 새로운 비밀번호
    val verifyPasswordInput: String = "",   // 비밀번호 확인
    val isSame: Boolean = true              // 새로운 비밀번호와 비밀번호 확인 일치 여부
)

enum class PasswordField {
    CURRENT,
    NEW,
    VERIFY
}

@HiltViewModel
class PasswordViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    companion object {
        private const val TAG = "PasswordViewModel"
    }

    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast = _toast.asSharedFlow()
    private val _navigateBack = MutableStateFlow(false)
    val navigateBack = _navigateBack.asStateFlow()

    private val _passwordUiState = MutableStateFlow(PasswordUiState())
    val passwordUiState = _passwordUiState.asStateFlow()

    /* 뒤로 가기 처리 함수 */
    fun setFalseNavigateBack() {
        _navigateBack.value = false
    }

    /* 입력 내용 변경 함수 */
    fun updatePassword(field: PasswordField, value: String) {
        _passwordUiState.update { state ->
            when (field) {
                PasswordField.CURRENT -> state.copy(currentPasswordInput = value)
                PasswordField.NEW -> {
                    val isSame = state.verifyPasswordInput == value
                    state.copy(newPasswordInput = value, isSame = isSame)
                }
                PasswordField.VERIFY -> {
                    val isSame = state.newPasswordInput == value
                    state.copy(verifyPasswordInput = value, isSame = isSame)
                }
            }
        }
    }

    /* 비밀번호 재확인 함수 */
    fun verifyPassword() {
        viewModelScope.launch {
            val request = UserDTO.VerifyPasswordRequest(passwordUiState.value.currentPasswordInput)
            repository.verifyPassword(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                Log.d(TAG, "비밀번호 재확인 성공")
                                editPassword()  // 비밀번호 변경
                            }
                            "E001" -> { // 사용자를 찾을 수 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 탈퇴 실패: 사용자를 찾을 수 없음")
                            }
                            "E002" -> { // 토큰 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 탈퇴 실패: 토큰 없음")
                            }
                            "E008" -> { // 현재 비밀번호 일치x
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 탈퇴 실패: 현재 비밀번호 일치 X")
                            }
                            "ERROR" -> { // 현재 비밀번호 입력X
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 탈퇴 실패: 현재 비밀번호 입력X")
                            }
                            else -> Log.d(TAG, "회원 탈퇴 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 비밀번호 변경 함수 */
    fun editPassword() {
        viewModelScope.launch {
            val request = UserDTO.EditPasswordRequest(_passwordUiState.value.currentPasswordInput, _passwordUiState.value.newPasswordInput)
            repository.editPassword(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _navigateBack.value = true  // 뒤로 가기
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "비밀번호 변경 성공: ${body.message}")
                            }
                            "E001" -> { // 사용자를 찾을 수 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "비밀번호 변경 실패: 사용자를 찾을 수 없음")
                            }
                            "E002" -> { // 토큰 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "비밀번호 변경 실패: 토큰 없음")
                            }
                            "E007" -> { // 새로운 비밀번호가 기존과 동일할 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "비밀번호 변경 실패: 새로운 비밀번호가 기존과 동일할 경우")
                            }
                            "E008" -> { // 현재 비밀번호 일치x
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "비밀번호 변경 실패: 현재 비밀번호 일치x")
                            }
                            "ERROR" -> { // 현재 비밀번호 or 새로운 비밀번호 입력 X
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "비밀번호 변경 실패: ${body.message}")
                            }
                            else -> Log.d(TAG, "비밀번호 변경 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }
}