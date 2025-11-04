package com.example.roadcode.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.PointDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.repository.PointRepository
import com.example.roadcode.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class PointViewModel @Inject constructor(private val repository: PointRepository, private val tokenRepository: TokenRepository) : ViewModel() {
    companion object {
        private const val TAG = "PointViewModel"
    }

    sealed class PointUiItem {
        data class DayHeader(val date: Int, val dayTotal: Int) : PointUiItem()
        data class Entry(val name: String, val amount: Int) : PointUiItem()
    }

    private val _points = MutableStateFlow(PointDTO.GetPointsByDateResponse(0, emptyList()))
    val points = _points.asStateFlow()
    private val _uiItems = MutableStateFlow<List<PointUiItem>>(emptyList())
    val uiItems = _uiItems.asStateFlow()
    private val _yearMonth = MutableStateFlow(YearMonth.now(ZoneId.of("Asia/Seoul")))
    val yearMonth = _yearMonth.asStateFlow()

    private val pointTypeMap = mapOf(
        "ATTENDANCE" to "출석",
        "PROBLEM_SOLVED" to "문제 풀이 성공",
        "DAILY_GOAL_COMPLETED" to "일일 목표 달성",
        "REVIEW" to "리뷰 작성",
        "ROADMAP_COMPLETED" to "로드맵 완성"
    )

    init {
        viewModelScope.launch {
            tokenRepository.tokenFlow.collect { token ->
                if (!token.isNullOrBlank()) {
                    getPointsByDate()
                }
            }
        }
    }

    /* 이전 달로 이동 */
    fun prevMonth() {
        _yearMonth.update { it.minusMonths(1) }
        getPointsByDate()
        Log.d(TAG, "이전 달로 이동: ${_yearMonth.value}")
    }

    /* 다음 달로 이동 */
    fun nextMonth() {
        _yearMonth.update { it.plusMonths(1) }
        getPointsByDate()
        Log.d(TAG, "다음 달로 이동: ${_yearMonth.value}")
    }

    /* 날짜별 포인트 내역 조회 함수 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getPointsByDate() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val start = yearMonth.value.atDay(1).format(formatter)
        val end = yearMonth.value.atEndOfMonth().format(formatter)

        viewModelScope.launch {
            repository.getPointsByDate(start, end).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val pointsData = body.data!!
                                _points.value = pointsData
                                formatUiItem()

                                Log.d(TAG, "날짜별 포인트 내역 조회 성공\n${pointsData}")
                            }
                            "E001" -> { // 토큰이 잘못된 경우
                                Log.d(TAG, "날짜별 포인트 내역 조회 실패: 토큰이 잘못된 경우")
                            }
                            "E026" -> { // groupBy 잘못 지정한 경우
                                Log.d(TAG, "날짜별 포인트 내역 조회 실패: groupBy 잘못 지정한 경우")
                            }
                            "E027" -> { // 날짜 형식이 잘못된 경우(start, end)
                                Log.d(TAG, "날짜별 포인트 내역 조회 실패: 날짜 형식이 잘못된 경우(start, end)")
                            }
                            else -> Log.d(TAG, "날짜별 포인트 내역 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }

    /* 출력용 포인트 내역 변환 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatUiItem() {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // 일자 오름차순 정렬
        val sortedDays = points.value.history
            .sortedBy { LocalDate.parse(it.date, dateFormatter) }

        // 월로 그룹
        val byMonth = sortedDays
            .groupBy { YearMonth.from(LocalDate.parse(it.date, dateFormatter)) }
            .toSortedMap()

        val items = mutableListOf<PointUiItem>()
        for ((ym, days) in byMonth) {
            for (d in days) {
                val localDate = LocalDate.parse(d.date, dateFormatter)
                items += PointUiItem.DayHeader(date = localDate.dayOfMonth, dayTotal = d.totalPoint)

                d.pointDetails.forEach { pd ->
                    items += PointUiItem.Entry(
                        name = pointTypeMap[pd.type] ?: pd.type,
                        amount = pd.point
                    )
                }
            }
        }

        _uiItems.value = items
    }
}