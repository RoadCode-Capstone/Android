package com.example.roadcode.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.UserDTO
import com.example.roadcode.data.repository.TokenRepository
import com.example.roadcode.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)

sealed class LoginEvent {
    object LoginSuccess: LoginEvent()
}

enum class LoginField { EMAIL, PASSWORD }

@HiltViewModel
class LoginViewModel @Inject constructor(private val tokenRepository: TokenRepository, private val repository: UserRepository) : ViewModel() {
    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _event = MutableSharedFlow<LoginEvent>()
    val event = _event
    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast = _toast.asSharedFlow()

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun init() {
        _loginUiState.value = LoginUiState()
    }

    /* 입력 필드 값 변경 함수 */
    fun updateInput(field: LoginField, value: String) {
        _loginUiState.update { state ->
            when (field) {
                LoginField.EMAIL ->     state.copy(email = value)
                LoginField.PASSWORD ->  state.copy(password = value)
            }
        }
    }

    /* 로그인 함수 */
    fun login(context: Context) {
        val request = UserDTO.EmailLoginRequest(email = loginUiState.value.email, password = loginUiState.value.password)

        viewModelScope.launch {
            repository.emailLogin(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val token = body.data!!.accessToken
//                                TokenManager.saveToken(context, token)   // DataStore에 token 저장
                                tokenRepository.saveToken(token) // DataStore에 token 저장

                                Log.d(TAG, "로그인 성공: token=${token}")
                                _event.emit(LoginEvent.LoginSuccess)    // 홈 화면으로 이동
                            }
                            "E001" -> { // 사용자를 찾을 수 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "로그인 실패: 사용자를 찾을 수 없음")
                            }
                            "E005" -> { // 닉네임 중복
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "로그인 실패: 닉네임 중복")
                            }
                            "E006" -> { // 비밀번호 일치 X
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "로그인 실패: 비밀번호 일치 X")
                            }
                            "ERROR" -> { // 이메일 또는 비밀번호 입력 X
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "로그인 실패: ${body.message}")
                            }
                            else -> Log.d(TAG, "로그인 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }
}