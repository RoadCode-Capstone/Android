package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.repository.LevelTestRepository
import com.example.roadcode.data.repository.RoadmapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

@HiltViewModel
class RoadmapViewModel @Inject constructor(private val repository: RoadmapRepository) : ViewModel() {
    companion object {
        private const val TAG = "RoadmapViewModel"
    }

    private val _roadmaps = MutableStateFlow<List<RoadmapDTO.roadmapsData>>(emptyList())    // 로드맵 목록
    val roadmaps = _roadmaps.asStateFlow()
    private val _roadmapId = MutableStateFlow<Long>(0)   // 로드맵 아이디
//    private val _roadmapId = MutableStateFlow<Long>(35)      // (테스트)
    val roadmapId = _roadmapId.asStateFlow()
    private val _roadmapInfo = MutableStateFlow<RoadmapDTO.roadmapData?>(null)   // 로드맵 정보
    val roadmapInfo = _roadmapInfo.asStateFlow()
    private val _problems = MutableStateFlow<List<RoadmapDTO.roadmapProblem>>(emptyList())  // 문제 목록
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
    private val _problemInfo = MutableStateFlow<ProblemDTO.ProblemData?>(null)  // 문제 정보
    val problemInfo = _problemInfo.asStateFlow()
    private val _problemIdx = MutableStateFlow<Int>(0) // 문제 인덱스
    val problemIdx = _problemIdx.asStateFlow()
    private val _progress = MutableStateFlow<String>("")   // 달성률 (소수점 첫째 자리까지 출력)
    val progress = _progress.asStateFlow()
    private val _status = MutableStateFlow<List<String>>(emptyList())  // 선택한 로드맵 상태
    val status = _status.asStateFlow()

    init {
        getRoadmaps(status.value)   // (테스트)

        setRoadmapId(35)    // (테스트)

        viewModelScope.launch { // 문제 인덱스, 문제 목록이 바뀔 때마다 실행
            combine(problemIdx, problems) { idx, list -> idx to list }
                .collect { (idx, list) ->
                    if (idx in list.indices) {
                        val problemId = list[idx].problemId
                        getProblem(problemId)   // 문제 정보 조회
                    }
            }
        }

        viewModelScope.launch { // 로드맵 정보, 문제 목록이 바뀔 때마다 실행
            combine(roadmapInfo, problems) { info, problems -> info to problems }
                .collect { (info, problems) ->
                    if (info != null && problems.isNotEmpty()) {
                        setProgress()   // 달성률 계산
                    }
                }
        }
    }

    /* 로드맵 아이디 설정 함수 */
    fun setRoadmapId(newId: Long) {
        _roadmapId.value = newId
        Log.d(TAG, "로드맵 아이디 변경: ${roadmapId.value}")

        getRoadmap()            // 로드맵 정보 조회
        getRoadmapProblems()    // 로드맵 문제 목록 조회
    }

    /* 문제 인덱스 설정 함수 */
    fun setProblemIdx(newIdx: Int) {
        _problemIdx.value = newIdx
        Log.d(TAG, "문제 인덱스 변경: ${problemIdx.value}")
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

    /* 달성률 계산 함수 */
    fun setProgress() {
        if (roadmapInfo.value!!.currentProblem.order + 1 == problems.value.size) {
            _progress.value = "100"
        }
        else {
            val percentage = ((roadmapInfo.value!!.currentProblem.order).toFloat() / problems.value.size) * 100
            _progress.value = if (percentage == 0f) {
                "0"
            }
            else {
                String.format("%.1f", percentage)
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

                        setProblemIdx(roadmapInfo.value!!.currentProblem.order) // 현재 풀어야 하는 문제 인덱스로 변경
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 문제 정보 조회 함수 */
    fun getProblem(problemId: Long) {
        viewModelScope.launch {
            val request = problemId

            repository.getProblem(request).collect() { result ->
                result
                    .onSuccess { problemInfo ->
                        _problemInfo.value = problemInfo
                        Log.d(TAG, "문제 정보: ${problemInfo}")
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 선택한 로드맵 상태 설정 함수 */
    fun setStatus(name: String, checked: Boolean) {
        _status.update { cur ->
            if (checked) {
                cur + name
            }
            else {
                cur.filterNot { it == name }
            }
        }

        Log.d(TAG, "선택한 로드맵 상태 변경: ${status.value}")

        getRoadmaps(status.value)
    }

    /* 로드맵 목록 조회 함수 */
    fun getRoadmaps(status: List<String>) {
        viewModelScope.launch {
            repository.getRoadmaps(status).collect() { result ->
                result
                    .onSuccess { roadmaps ->
                        _roadmaps.value = roadmaps
                        Log.d(TAG, "로드맵 목록: ${roadmaps}")
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }
}