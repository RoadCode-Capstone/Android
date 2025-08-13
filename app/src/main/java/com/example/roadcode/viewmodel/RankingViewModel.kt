package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.PointDTO
import com.example.roadcode.data.repository.PointRepository
import com.example.roadcode.data.repository.RoadmapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(private val repository: PointRepository) : ViewModel() {
    companion object {
        private const val TAG = "RankingViewModel"
    }

    private val _myRank = MutableStateFlow<Int>(0)  // 사용자 순위
    val myRank = _myRank.asStateFlow()
    private val _ranks = MutableStateFlow<List<PointDTO.RankData>>(emptyList()) // 전체 순위 목록
    val ranks = _ranks.asStateFlow()
    private val _myRankInfo = MutableStateFlow<PointDTO.RankData?>(null) // 내 순위 정보
    val myRankInfo = _myRankInfo.asStateFlow()
    private val _topPercent = MutableStateFlow<Int>(0)  // 상위 퍼센트
    val topPercent = _topPercent.asStateFlow()

    init {
        getRanking("2025-06-06", "2025-08-06")
    }

    /* 순위 조회 함수 */
    fun getRanking(start: String, end: String) {
        viewModelScope.launch {
            repository.getRanking(start, end).collect() { result ->
                result
                    .onSuccess { rankData ->
                        _myRank.value = rankData.myRank
                        _ranks.value = rankData.ranks
                        Log.d(TAG, "사용자 순위: ${rankData.myRank}")
                        Log.d(TAG, "전체 순위 목록: ${rankData.ranks}")

                        _myRankInfo.value = ranks.value[myRank.value - 1]
                        calculateTopPercent()
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 상위 퍼센트 계산 함수: 1등이면 0%, 꼴등이면 100%로 설정함 */
    private fun calculateTopPercent() {
        if (myRank.value == 1) {
            _topPercent.value =  0
        }
        else {
            _topPercent.value = ((myRank.value.toDouble() / ranks.value.size) * 100).toInt()
        }
    }

}