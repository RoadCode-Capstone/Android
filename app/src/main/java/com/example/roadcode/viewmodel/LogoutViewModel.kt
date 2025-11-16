package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.repository.TokenRepository
import com.example.roadcode.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LogoutEvent {
    object LogoutSuccess: LogoutEvent()
}


@HiltViewModel
class LogoutViewModel @Inject constructor(private val repository: UserRepository, private val tokenRepository: TokenRepository) : ViewModel() {
    companion object {
        private const val TAG = "LogoutViewModel"
    }

    private val _event = MutableSharedFlow<LogoutEvent>()
    val event = _event
    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast = _toast.asSharedFlow()

    /* 로그아웃 함수 */
    fun logout() {
        viewModelScope.launch {
            repository.logout().collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                tokenRepository.clearToken()
                                Log.d(TAG, "로그아웃 성공")
                                _event.emit(LogoutEvent.LogoutSuccess)    // 로그인 화면으로 이동
                            }
                            "E002" -> { // 토큰 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "로그아웃 실패: 토큰 없음")
                            }
                            else -> Log.d(TAG, "로그아웃 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }
}