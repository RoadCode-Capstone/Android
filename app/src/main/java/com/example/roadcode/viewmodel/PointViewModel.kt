package com.example.roadcode.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.PointDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.repository.PointRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class PointViewModel @Inject constructor(private val repository: PointRepository) : ViewModel() {
    companion object {
        private const val TAG = "PointViewModel"
    }

    sealed class PointUiItem {
        data class MonthHeader(val yearMonth: YearMonth) : PointUiItem()
        data class DayHeader(val date: Int, val dayTotal: Int) : PointUiItem()
        data class Entry(val name: String, val amount: Int) : PointUiItem()
    }

    private val _points = MutableStateFlow(PointDTO.GetPointsByDateResponse(0, emptyList()))
    val points = _points.asStateFlow()
    private val _uiItems = MutableStateFlow<List<PointUiItem>>(emptyList())
    val uiItems = _uiItems.asStateFlow()

    private val pointTypeMap = mapOf(
        "ATTENDANCE" to "출석",
        "PROBLEM_SOLVED" to "문제 풀이 성공",
        "DAILY_GOAL_COMPLETED" to "일일 목표 달성",
        "REVIEW" to "리뷰 작성",
        "ROADMAP_COMPLETED" to "로드맵 완성"
    )

    init {
        getPointsByDate("2025-07-01", "2025-08-31")
    }

    /* 날짜별 포인트 내역 조회 함수 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getPointsByDate(start: String, end: String) {
        viewModelScope.launch {
            repository.getPointsByDate(start, end).collect() { result ->
                result
                    .onSuccess { pointsData ->
                        _points.value = pointsData
                        Log.d(TAG, "날짜별 포인트 내역: ${points.value}")
                        formatUiItem()
                    }
                    .onFailure { e ->
                        e.printStackTrace()
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
            items += PointUiItem.MonthHeader(ym)

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