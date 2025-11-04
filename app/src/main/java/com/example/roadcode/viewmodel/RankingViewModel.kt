package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.PointDTO
import com.example.roadcode.data.repository.PointRepository
import com.example.roadcode.data.repository.RoadmapRepository
import com.example.roadcode.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(private val repository: PointRepository, private val tokenRepository: TokenRepository) : ViewModel() {
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
        viewModelScope.launch {
            tokenRepository.tokenFlow.collect { token ->
                if (!token.isNullOrBlank()) {
                    getRanking("2025-06-06", "2025-08-06")  // TODO: 현재 월로 변경
                    cancel()
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

    /* 순위 조회 함수 */
    fun getRanking(start: String, end: String) {
        viewModelScope.launch {
            repository.getRanking(start, end).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val rankData = body.data!!
                                _myRank.value = rankData.myRank
                                _ranks.value = rankData.ranks
                                _myRankInfo.value = ranks.value[myRank.value - 1]
                                calculateTopPercent()

                                Log.d(TAG, "순위 조회 성공\n사용자 순위: ${rankData.myRank}\n전체 순위 목록: ${rankData.ranks}")
                            }
                            "E001" -> { // 토큰이 잘못된 경우 / 사용자가 순위에 존재하지 않는 경우
                                Log.d(TAG, "순위 조회 실패: 토큰이 잘못된 경우 / 사용자가 순위에 존재하지 않는 경우")
                            }
                            "E027" -> { // start, end 날짜 형식이 잘못된 경우
                                Log.d(TAG, "순위 조회 실패: start, end 날짜 형식이 잘못된 경우")
                            }
                            else -> Log.d(TAG, "순위 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }
}