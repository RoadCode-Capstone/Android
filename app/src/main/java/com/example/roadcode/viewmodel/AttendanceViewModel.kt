package com.example.roadcode.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.repository.AttendanceRepository
import com.example.roadcode.data.repository.PointRepository
import com.example.roadcode.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val repository: AttendanceRepository, private val tokenRepository: TokenRepository, private val pointRepository: PointRepository) : ViewModel() {
    companion object {
        private val TAG = "AttendanceViewModel"
    }

    private val _attendanceCnt = MutableStateFlow(0)
    val attendanceCnt = _attendanceCnt.asStateFlow()

    init {
        viewModelScope.launch {
            tokenRepository.tokenFlow.collect { token ->
                if (!token.isNullOrBlank()) {
                    checkAttendance()
                }
            }
        }
    }

    /* 출석 체크 함수 */
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

    /* 한 달 출석 개수 조회 함수 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonthAttendanceCnt(yearMonth: YearMonth) {
        val startDate = yearMonth.atDay(1).toString()
        val endDate = yearMonth.atEndOfMonth().toString()

        viewModelScope.launch {
            pointRepository.getPointsByType(startDate, endDate).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _attendanceCnt.value = body.data!!.history.find { it.type == "ATTENDANCE"}?.dates?.size ?: 0

                                Log.d(TAG, "한 달 출석 개수 조회 성공: ${attendanceCnt.value}")
                            }
                            "E001" -> { // 토큰이 잘못된 경우
                                Log.d(TAG, "한 달 출석 개수 조회 실패: 토큰이 잘못된 경우")
                            }
                            "E026" -> { // groupBy 잘못 지정한 경우
                                Log.d(TAG, "한 달 출석 개수 조회 실패: groupBy 잘못 지정한 경우")
                            }
                            "E027" -> { // 날짜 형식이 잘못된 경우(start, end)
                                Log.d(TAG, "한 달 출석 개수 조회 실패: 날짜 형식이 잘못된 경우(start, end)")
                            }
                            else -> Log.d(TAG, "한 달 출석 개수 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }
}