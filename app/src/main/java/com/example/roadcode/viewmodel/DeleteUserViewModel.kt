package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.UserDTO
import com.example.roadcode.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeleteUserUiState(
    val currentPasswordInput: String = ""   // 현재 비밀번호
)

@HiltViewModel
class DeleteUserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    companion object {
        private val TAG = "DeleteUserViewModel"
    }

    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast = _toast.asSharedFlow()

    private val _deleteUserUiState = MutableStateFlow(DeleteUserUiState())
    val deleteUserUiState = _deleteUserUiState.asStateFlow()

    /* 입력 내용 변경 함수 */
    fun updatePassword(value: String) {
        _deleteUserUiState.update { it.copy(currentPasswordInput = value) }
    }

    /* 비밀번호 재확인 함수 */
    fun verifyPassword() {
        viewModelScope.launch {
            val request = UserDTO.VerifyPasswordRequest(deleteUserUiState.value.currentPasswordInput)
            repository.verifyPassword(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "비밀번호 재확인 성공")
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
                                Log.d(TAG, "회원 탈퇴 실패: ${body.message}")
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

    /* 회원 탈퇴 함수 */
    fun deleteUser() {
        viewModelScope.launch {
            val request = UserDTO.DeleteMemberRequest(deleteUserUiState.value.currentPasswordInput)
            repository.deleteMember(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _toast.emit("회원 탈퇴에 성공했습니다.")
                                Log.d(TAG, "회원 탈퇴 성공")
                            }
                            "E001" -> { // 사용자를 찾을 수 없음
                                _toast.emit("사용자를 찾을 수 없습니다.")
                                Log.d(TAG, "회원 탈퇴 실패: 사용자를 찾을 수 없음")
                            }
                            "E002" -> { // 토큰 없음
                                _toast.emit("토큰이 없습니다.")
                                Log.d(TAG, "회원 탈퇴 실패: 토큰 없음")
                            }
                            "E006" -> { // 비밀번호 일치 X
                                _toast.emit("비밀번호가 일치하지 않습니다.")
                                Log.d(TAG, "회원 탈퇴 실패: 비밀번호 일치 X")
                            }
                            "ERROR" -> { // 비밀번호 입력X
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 탈퇴 실패: ${body.message}")
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
}
