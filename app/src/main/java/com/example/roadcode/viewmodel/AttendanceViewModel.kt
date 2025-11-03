package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.repository.AttendanceRepository
import com.example.roadcode.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val repository: AttendanceRepository) : ViewModel() {
    companion object {
        private val TAG = "AttendanceViewModel"
    }

    /* 출석 체크 */
    fun checkAttendance() {
        viewModelScope.launch {
            repository.checkAttendance().collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                Log.d(TAG, "출석 체크 성공: ${body.message}")
                            }
                            "E001" -> { // 사용자 토큰이 잘못된 경우
                                Log.d(TAG, "출석 체크 실패: 사용자 토큰이 잘못된 경우")
                            }
                            else -> Log.d(TAG, "출석 체크 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }
}