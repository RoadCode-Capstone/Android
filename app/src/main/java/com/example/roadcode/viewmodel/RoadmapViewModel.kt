package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.repository.LevelTestRepository
import com.example.roadcode.data.repository.RoadmapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoadmapViewModel @Inject constructor(private val repository: RoadmapRepository) : ViewModel() {
    companion object {
        private const val TAG = "RoadmapViewModel"
    }

    private val _roadmapId = MutableStateFlow<Long>(0)   // 로드맵 아이디
//    private val _roadmapId = MutableStateFlow<Long>(35)      // (테스트)
    val roadmapId = _roadmapId.asStateFlow()
    private val _roadmapInfo = MutableStateFlow<RoadmapDTO.roadmapData?>(null)   // 로드맵 정보
    val roadmapInfo = _roadmapInfo.asStateFlow()
    private val _problems = MutableStateFlow<List<RoadmapDTO.roadmapProblem>>(emptyList())  // 문제 정보
//    private val _problems = MutableStateFlow<List<RoadmapDTO.roadmapProblem>>(listOf(
//        RoadmapDTO.roadmapProblem(771, 2195, 0, "IN_PROGRESS"),
//        RoadmapDTO.roadmapProblem(772, 14, 1, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(773, 20, 2, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(774, 62, 3, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(775, 98, 4, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(776, 10, 5, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(777, 209, 6, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(778, 289, 7, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(779, 331, 8, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(780, 15, 9, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(781, 45, 10, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(782, 122, 11, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(783, 11, 12, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(784, 23, 13, "NOT_STARTED"),
//        RoadmapDTO.roadmapProblem(785, 7, 14, "NOT_STARTED")))    // (테스트)
    val problems = _problems.asStateFlow()

    init {
        setRoadmapId(35)    // (테스트)

        // roadmapId가 변경될 때마다 로드맵 정보 조회 및 문제 목록 조회 실행
        viewModelScope.launch {
            roadmapId.collect {
                getRoadmap()
                getRoadmapProblems()
            }
        }
    }

    /* 로드맵 아이디 설정 함수 */
    fun setRoadmapId(newId: Long) {
        _roadmapId.value = newId
        Log.d(TAG, "로드맵 아이디 변경: ${roadmapId.value}")
    }

    /* 로드맵 정보 조회 함수 */
    fun getRoadmap() {
        viewModelScope.launch {
            val request = roadmapId.value

            repository.getRoadmap(request).collect() { result ->
                result
                    .onSuccess { roadmapInfo ->
                        _roadmapInfo.value = roadmapInfo
                        Log.d(TAG, "로드맵 정보: ${roadmapInfo}")
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 로드맵 문제 목록 조회 함수 */
    fun getRoadmapProblems() {
        viewModelScope.launch {
            val request = roadmapId.value

            repository.getRoadmapProblems(request).collect() { result ->
                result
                    .onSuccess { problems ->
                        _problems.value = problems
                        Log.d(TAG, "로드맵 문제 목록: ${problems}")
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }
}