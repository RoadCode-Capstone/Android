package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.repository.RoadmapRepository
import com.example.roadcode.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

@HiltViewModel
class RoadmapViewModel @Inject constructor(private val repository: RoadmapRepository, private val tokenRepository: TokenRepository) : ViewModel() {
    companion object {
        private const val TAG = "RoadmapViewModel"
    }

    private val _roadmaps = MutableStateFlow<List<RoadmapDTO.RoadmapsData>>(emptyList())    // 로드맵 목록
    val roadmaps = _roadmaps.asStateFlow()
    private val _roadmapId = MutableStateFlow<Long>(0)   // 로드맵 아이디
//    private val _roadmapId = MutableStateFlow<Long>(35)      // (테스트)
    val roadmapId = _roadmapId.asStateFlow()
    private val _roadmapStatus = MutableStateFlow("")   // 로드맵 상태
    val roadmapStatus = _roadmapStatus.asStateFlow()
    private val _roadmapInfo = MutableStateFlow(RoadmapDTO.RoadmapData())   // 로드맵 정보
    val roadmapInfo = _roadmapInfo.asStateFlow()
    private val _problems = MutableStateFlow<List<RoadmapDTO.RoadmapProblem>>(emptyList())  // 문제 목록
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
        viewModelScope.launch {
            // 문제 인덱스, 문제 목록이 바뀔 때마다 문제 정보 조회
            launch {
                combine(problemIdx, problems) { idx, list -> idx to list }
                    .collect { (idx, list) ->
                        if (idx in list.indices) {
                            val problemId = list[idx].problemId
                            getProblem(problemId)
                        }
                    }
            }

            // 로드맵 정보, 문제 목록이 바뀔 때마다 실행
            launch {
                combine(roadmapInfo, problems) { info, problems -> info to problems }
                    .collect { (info, problems) ->
                        if (info != null && problems.isNotEmpty()) {
                            setProgress()
                        }
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

    /* 로드맵 상태 설정 함수 */
    fun setRoadmapStatus(newStatus: String) {
        _roadmapStatus.value = newStatus
        Log.d(TAG, "로드맵 상태 변경: ${roadmapStatus.value}")
    }

    /* 문제 인덱스 설정 함수 */
    fun setProblemIdx(newIdx: Int) {
        _problemIdx.value = newIdx
        Log.d(TAG, "문제 인덱스 변경: ${problemIdx.value}")
    }

    /* 로드맵 정보 조회 함수 */
    fun getRoadmap() {
        val request = roadmapId.value

        viewModelScope.launch {
            repository.getRoadmap(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val roadmapInfo = body.data!!
                                _roadmapInfo.value = roadmapInfo

                                Log.d(TAG, "로드맵 정보 조회 성공\n${roadmapInfo}")
                            }
                            "E001" -> { // 사용자 토큰이 잘못된 경우
                                Log.d(TAG, "로드맵 정보 조회 실패: 사용자 토큰이 잘못된 경우")
                            }
                            "E018" -> { // 로드맵을 찾을 수 없는 경우(로드맵 ID가 잘못됐거나 생성되지 않음)
                                Log.d(TAG, "로드맵 정보 조회 실패: 로드맵을 찾을 수 없는 경우(로드맵 ID가 잘못됐거나 생성되지 않음)")
                            }
                            "E021" -> { // 회원이 해당 로드맵의 주인이 아닌 경우
                                Log.d(TAG, "로드맵 정보 조회 실패: 회원이 해당 로드맵의 주인이 아닌 경우")
                            }
                            else -> Log.d(TAG, "로드맵 정보 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
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
        val request = roadmapId.value

        viewModelScope.launch {
            repository.getRoadmapProblems(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val problems = body.data!!.roadmapProblems
                                _problems.value = problems

                                setProblemIdx(roadmapInfo.value!!.currentProblem.order) // 현재 풀어야 하는 문제 인덱스로 변경

                                Log.d(TAG, "로드맵 문제 목록 조회 성공\n${problems}")
                            }
                            "E001" -> { // 사용자 토큰이 잘못된 경우
                                Log.d(TAG, "로드맵 문제 목록 조회 실패: 사용자 토큰이 잘못된 경우")
                            }
                            "E021" -> { // 회원이 해당 로드맵의 주인이 아닌 경우
                                Log.d(TAG, "로드맵 문제 목록 조회 실패: 회원이 해당 로드맵의 주인이 아닌 경우")
                            }
                            else -> Log.d(TAG, "로드맵 문제 목록 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }

    /* 문제 정보 조회 함수 */
    fun getProblem(problemId: Long) {
        val request = problemId

        viewModelScope.launch {
            repository.getProblem(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val problemInfo = body.data!!
                                _problemInfo.value = problemInfo

                                Log.d(TAG, "로드맵 정보 조회 성공\n${problemInfo}")
                            }
                            "E001" -> { // 사용자를 찾을 수 없음
                                Log.d(TAG, "로드맵 정보 조회 실패: 사용자를 찾을 수 없음")
                            }
                            "E002" -> { // 토큰 없음
                                Log.d(TAG, "로드맵 정보 조회 실패: 토큰 없음")
                            }
                            "E014" -> { // 문제 id가 잘못된 경우(문제가 없는 경우)
                                Log.d(TAG, "로드맵 정보 조회 실패: 문제 id가 잘못된 경우(문제가 없는 경우)")
                            }
                            else -> Log.d(TAG, "로드맵 정보 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }

    /* 로드맵 포기 함수 */
    fun giveUpRoadmap() {
        val request = roadmapId.value

        viewModelScope.launch {
            repository.giveUpRoadmap(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                Log.d(TAG, "로드맵 포기 성공")
                            }
                            "E028" -> { // 완료된 로드맵을 포기하려는 경우
                                Log.d(TAG, "로드맵 포기 실패: 완료된 로드맵을 포기하려는 경우")
                            }
                            "E029" -> { // 이미 포기한 로드맵을 포기하려는 경우
                                Log.d(TAG, "로드맵 포기 실패: 이미 포기한 로드맵을 포기하려는 경우")
                            }
                            else -> Log.d(TAG, "로드맵 포기 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
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
    fun getRoadmaps(status: List<String>?) {
        viewModelScope.launch {
            repository.getRoadmaps(status).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val roadmaps = body.data!!.roadmaps
                                _roadmaps.value = roadmaps

                                Log.d(TAG, "로드맵 목록 조회 성공\n${roadmaps}")
                            }
                            "E001" -> { // 사용자 email이 잘못된 경우
                                Log.d(TAG, "로드맵 목록 조회 실패: 사용자 email이 잘못된 경우")
                            }
                            "ERROR" -> { // 로드맵 STATUS(파라미터)가 잘못된 경우
                                Log.d(TAG, "로드맵 목록 조회 실패: 로드맵 STATUS(파라미터)가 잘못된 경우")
                            }
                            else -> Log.d(TAG, "로드맵 목록 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }
}