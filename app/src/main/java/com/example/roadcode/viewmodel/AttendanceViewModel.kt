package com.example.roadcode.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.data.repository.AttendanceRepository
import com.example.roadcode.data.repository.PointRepository
import com.example.roadcode.data.repository.SubmissionRepository
import com.example.roadcode.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

enum class AttendanceType {
    // 로그인 X (0%), 로그인 O 일일 학습 목표 달성 X (50%), 일일 학습 목표 달성 O (100%)
    NOT_LOGIN, NOT_GOAL, PERFECT
}

data class AttendanceUiState(
    val attendanceCnt: Int = 0,                                             // 한 달 출석 개수
    val submissions: List<SubmissionDTO.MySubmissionsData> = emptyList(),   // 한 달 풀이 목록
    val attendanceTypeMap: Map<String, AttendanceType> = emptyMap()         // 한 달 날짜별 출석 유형
)

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val repository: AttendanceRepository, private val tokenRepository: TokenRepository, private val pointRepository: PointRepository, private val submissionRepository: SubmissionRepository) : ViewModel() {
    companion object {
        private val TAG = "AttendanceViewModel"
    }

    private val _attendanceUiState = MutableStateFlow(AttendanceUiState())
    val attendanceUiState = _attendanceUiState.asStateFlow()

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

    /* 한 달 출석 개수 조회 & 한 달 출석 유형 조회 함수 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonthAttendanceInfo(yearMonth: YearMonth) {
        val startDate = yearMonth.atDay(1).toString()
        val endDate = yearMonth.atEndOfMonth().toString()

        viewModelScope.launch {
            pointRepository.getPointsByType(startDate, endDate).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val typeMap = mutableMapOf<String, AttendanceType>()

                                // 로그인 O 일일 학습 목표 달성 X 날짜 리스트
                                val loginDates = body.data!!.history
                                    ?.find { it.type == "ATTENDANCE" }
                                    ?.dates
                                    ?.map { LocalDate.parse(it).dayOfMonth.toString() }
                                    ?.toSet() ?: emptySet()

                                // 일일 학습 목표 달성 O 날짜 리스트
                                val perfectDates = body.data!!.history
                                    ?.find { it.type == "DAILY_GOAL_COMPLETED" }
                                    ?.dates
                                    ?.map { LocalDate.parse(it).dayOfMonth.toString() }
                                    ?.toSet() ?: emptySet()

                                // 한 달 전체 날짜 생성
                                val allDates = (1..yearMonth.lengthOfMonth()).map { it.toString() }

                                allDates.forEach { date ->
                                    val type = when {
                                        date in perfectDates    -> AttendanceType.PERFECT
                                        date in loginDates      -> AttendanceType.NOT_GOAL
                                        else                    -> AttendanceType.NOT_LOGIN
                                    }
                                    typeMap[date] = type
                                }

                                _attendanceUiState.update { it. copy(
                                    attendanceCnt = body.data!!.history.find { it.type == "ATTENDANCE"}?.dates?.size ?: 0,
                                    attendanceTypeMap = typeMap
                                ) }

                                Log.d(TAG, "한 달 출석 개수 & 유형 조회 성공: ${body.data.history}")
                                Log.d(TAG, "한 달 출석 개수 & 유형 조회 성공: typeMap ${typeMap}")
                            }
                            "E001" -> { // 토큰이 잘못된 경우
                                Log.d(TAG, "한 달 출석 개수 & 유형 조회 실패: 토큰이 잘못된 경우")
                            }
                            "E026" -> { // groupBy 잘못 지정한 경우
                                Log.d(TAG, "한 달 출석 개수 & 유형 조회 실패: groupBy 잘못 지정한 경우")
                            }
                            "E027" -> { // 날짜 형식이 잘못된 경우(start, end)
                                Log.d(TAG, "한 달 출석 개수 & 유형 조회 실패: 날짜 형식이 잘못된 경우(start, end)")
                            }
                            else -> Log.d(TAG, "한 달 출석 개수 & 유형 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }

    /* 한 달 풀이 목록 조회 함수 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonthSubmissions(yearMonth: YearMonth) {
        val startDate = yearMonth.atDay(1).toString()
        val endDate = yearMonth.atEndOfMonth().toString()

        viewModelScope.launch {
            submissionRepository.getMySubmissions(startDate, endDate, true).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _attendanceUiState.update { it.copy(
                                    submissions = body.data?.history ?: emptyList()
                                ) }

                                Log.d(TAG, "한 달 풀이 목록 조회 성공: ${attendanceUiState.value.submissions}")
                            }
                            "E001" -> { // 토큰이 잘못된 경우
                                Log.d(TAG, "한 달 풀이 목록 조회 실패: 토큰이 잘못된 경우")
                            }
                            "E027" -> { // 날짜 형식이 잘못된 경우(start, end)
                                Log.d(TAG, "한 달 풀이 목록 조회 실패: 날짜 형식이 잘못된 경우(start, end)")
                            }
                            else -> Log.d(TAG, "한 달 풀이 목록 조회 실패: 알 수 없는 오류")
                        }

                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }
}