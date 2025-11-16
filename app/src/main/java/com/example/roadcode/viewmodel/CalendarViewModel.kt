package com.example.roadcode.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.YearMonth
import java.time.ZoneId
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {
    companion object {
        private val TAG = "CalendarViewModel"
    }

    private val _yearMonth = MutableStateFlow(YearMonth.now(ZoneId.of("Asia/Seoul")))
    val yearMonth = _yearMonth.asStateFlow()

    /* 이전 달로 이동 */
    fun prevMonth() {
        _yearMonth.update { it.minusMonths(1) }
        Log.d(TAG, "이전 달로 이동: ${_yearMonth.value}")
    }

    /* 다음 달로 이동 */
    fun nextMonth() {
        _yearMonth.update { it.plusMonths(1) }
        Log.d(TAG, "다음 달로 이동: ${_yearMonth.value}")
    }
}