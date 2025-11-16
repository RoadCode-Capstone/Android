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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _navigateBack = MutableStateFlow(false)
    val navigateBack = _navigateBack.asStateFlow()
    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast = _toast.asSharedFlow()

    private val _roadmaps = MutableStateFlow<List<RoadmapDTO.RoadmapsData>>(emptyList())    // 로드맵 목록
    val roadmaps = _roadmaps.asStateFlow()
    private val _roadmapId = MutableStateFlow<Long>(0)   // 로드맵 아이디
    val roadmapId = _roadmapId.asStateFlow()
    private val _roadmapStatus = MutableStateFlow("")   // 로드맵 상태
    val roadmapStatus = _roadmapStatus.asStateFlow()
    private val _roadmapInfo = MutableStateFlow(RoadmapDTO.RoadmapData())   // 로드맵 정보
    val roadmapInfo = _roadmapInfo.asStateFlow()
    private val _problems = MutableStateFlow<List<RoadmapDTO.RoadmapProblem>>(emptyList())  // 문제 목록
    val problems = _problems.asStateFlow()
    private val _problemInfo = MutableStateFlow<ProblemDTO.ProblemData?>(null)  // 문제 정보
    val problemInfo = _problemInfo.asStateFlow()
    private val _problemIdx = MutableStateFlow<Int>(0) // 조회할 문제 인덱스 (문제 목록에서의)
    val problemIdx = _problemIdx.asStateFlow()
    private val _curProblemIdx = MutableStateFlow(0)    // 현재 풀어야 할 문제 인덱스 (문제 목록에서의)
    val curProblemIdx = _curProblemIdx.asStateFlow()
    private val _progress = MutableStateFlow<String>("")   // 달성률 (소수점 첫째 자리까지 출력)
    val progress = _progress.asStateFlow()
    private val _status = MutableStateFlow<List<String>>(emptyList())  // 선택한 로드맵 상태
    val status = _status.asStateFlow()

    init {
        viewModelScope.launch {
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

    /* 뒤로 가기 처리 함수 */
    fun setFalseNavigateBack() {
        _navigateBack.value = false
    }

    /* 로드맵 아이디 설정 함수 */
    fun setRoadmapId(newId: Long) {
        _roadmapId.value = newId
        Log.d(TAG, "로드맵 아이디 변경: ${roadmapId.value}")

        getRoadmap()            // 로드맵 정보 조회
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

        getProblem(problems.value[problemIdx.value].problemId)
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

                                val curProblemId = roadmapInfo.currentProblem.problemId                       // 현재 풀어야 하는 문제 아이디
                                _curProblemIdx.value = problems.value.indexOfFirst { it.problemId == curProblemId } // 현재 풀어야 하는 문제 인덱스 설정

                                Log.d(TAG, "로드맵 정보 조회 성공\n${roadmapInfo}")

                                // 로드맵 문제 목록 조회
                                repository.getRoadmapProblems(request).collect() { result ->
                                    result
                                        .onSuccess { body ->
                                            when (body.code) {
                                                "SUCCESS" -> {
                                                    val problems = body.data!!.roadmapProblems
                                                    _problems.value = problems

                                                    Log.d(TAG, "로드맵 문제 목록 조회 성공\n${problems}")

                                                    setProblemIdx(curProblemIdx.value)  // 현재 풀어야 하는 문제 인덱스로 변경 및 문제 정보 조회
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
                            "E001" -> { // 사용자 토큰이 잘못된 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "로드맵 정보 조회 실패: 사용자 토큰이 잘못된 경우")
                            }
                            "E018" -> { // 로드맵을 찾을 수 없는 경우(로드맵 ID가 잘못됐거나 생성되지 않음)
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "로드맵 정보 조회 실패: 로드맵을 찾을 수 없는 경우(로드맵 ID가 잘못됐거나 생성되지 않음)")
                            }
                            "E021" -> { // 회원이 해당 로드맵의 주인이 아닌 경우
                                _toast.emit(body.message ?: "")
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

    /* 달성률 계산 함수 (문제 목록에서 현재 문제 인덱스를 찾아 나누기 전체 문제 수 곱하기 100) */
    fun setProgress() {
        val curProblemId = roadmapInfo.value.currentProblem.problemId                       // 현재 풀어야 하는 문제 아이디
        _curProblemIdx.value = problems.value.indexOfFirst { it.problemId == curProblemId }

        if (curProblemIdx.value + 1 == problems.value.size) {
            _progress.value = "100"
        }
        else {
            val percentage = (curProblemIdx.value.toFloat() / problems.value.size) * 100
            _progress.value = if (percentage == 0f) {
                "0"
            }
            else {
                String.format("%.1f", percentage)
            }
        }

        Log.d(TAG, "달성률 계산: ${progress.value}")
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

                                Log.d(TAG, "문제 정보 조회 성공\n${problemInfo}")
                            }
                            "E001" -> { // 사용자를 찾을 수 없음
                                Log.d(TAG, "문제 정보 조회 실패: 사용자를 찾을 수 없음")
                            }
                            "E002" -> { // 토큰 없음
                                Log.d(TAG, "문제 정보 조회 실패: 토큰 없음")
                            }
                            "E014" -> { // 문제 id가 잘못된 경우(문제가 없는 경우)
                                Log.d(TAG, "문제 정보 조회 실패: 문제 id가 잘못된 경우(문제가 없는 경우)")
                            }
                            else -> Log.d(TAG, "문제 정보 조회 실패: 알 수 없는 오류")
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
                                _navigateBack.value = true
                                _toast.emit("로드맵을 포기했어요")
                                Log.d(TAG, "로드맵 포기 성공")
                            }
                            "E028" -> { // 완료된 로드맵을 포기하려는 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "로드맵 포기 실패: 완료된 로드맵을 포기하려는 경우")
                            }
                            "E029" -> { // 이미 포기한 로드맵을 포기하려는 경우
                                _toast.emit(body.message ?: "")
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

                                val continueRoadmap = roadmaps.find { it.status == "IN_PROGRESS" }
                                continueRoadmap?.let {  // 진행 중인 로드맵은 1개이므로 진행 중 상태의 로드맵 정보 조회
                                    setRoadmapId(it.roadmapId)
                                    setProgress()
                                }

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

    /* 추가 문제 추천 함수 */
    fun addMoreProblems() {
        viewModelScope.launch {
            repository.addMoreProblems(roadmapId.value).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val roadmapInfo = body.data!!
                                _roadmapInfo.value = roadmapInfo

                                val curProblemId = roadmapInfo.currentProblem.problemId                       // 현재 풀어야 하는 문제 아이디
                                _curProblemIdx.value = problems.value.indexOfFirst { it.problemId == curProblemId } // 현재 풀어야 하는 문제 인덱스 설정

                                _toast.emit(body.message ?: "문제를 추가했어요")
                                Log.d(TAG, "추가 문제 추천 성공\n${roadmapInfo}")

                                // 로드맵 문제 목록 조회
                                repository.getRoadmapProblems(roadmapId.value).collect() { result ->
                                    result
                                        .onSuccess { body ->
                                            when (body.code) {
                                                "SUCCESS" -> {
                                                    val problems = body.data!!.roadmapProblems
                                                    _problems.value = problems

                                                    Log.d(TAG, "로드맵 문제 목록 조회 성공\n${problems}")

                                                    setProblemIdx(curProblemIdx.value)  // 현재 풀어야 하는 문제 인덱스로 변경 및 문제 정보 조회
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
                            "E015" -> { // 언어 종류를 java/python/c(대소문자 상관 없음) 외에 다른 걸 입력한 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "추가 문제 추천 실패: 언어 종류를 java/python/c(대소문자 상관 없음) 외에 다른 걸 입력한 경우")
                            }
                            "E019" -> { // 로드맵 종류(type)를 algorithm, language(대소문자 상관 없음) 외에 다른 걸 입력할 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "추가 문제 추천 실패: 로드맵 종류(type)를 algorithm, language(대소문자 상관 없음) 외에 다른 걸 입력할 경우")
                            }
                            "E020" -> { // 존재하지 않는 알고리즘 입력 시
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "추가 문제 추천 실패: 존재하지 않는 알고리즘 입력 시")
                            }
                            else -> Log.d(TAG, "추가 문제 추천 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 개념 강화 문제 추가 함수 */
    fun addReinforceProblem() {
        val request = RoadmapDTO.AddReinforceProblemRequest(roadmapInfo.value.currentProblem.problemId)

        viewModelScope.launch {
            repository.addReinforceProblem(roadmapId.value, request).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val roadmapInfo = body.data!!
                                _roadmapInfo.value = roadmapInfo

                                val curProblemId = roadmapInfo.currentProblem.problemId                       // 현재 풀어야 하는 문제 아이디
                                _curProblemIdx.value = problems.value.indexOfFirst { it.problemId == curProblemId } // 현재 풀어야 하는 문제 인덱스 설정

                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "개념 강화 문제 추가 성공\n${roadmapInfo}")

                                // 로드맵 문제 목록 조회
                                repository.getRoadmapProblems(roadmapId.value).collect() { result ->
                                    result
                                        .onSuccess { body ->
                                            when (body.code) {
                                                "SUCCESS" -> {
                                                    val problems = body.data!!.roadmapProblems
                                                    _problems.value = problems

                                                    Log.d(TAG, "로드맵 문제 목록 조회 성공\n${problems}")

                                                    setProblemIdx(curProblemIdx.value)  // 현재 풀어야 하는 문제 인덱스로 변경 및 문제 정보 조회
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
                            "E015" -> { // 언어 종류를 java/python/c(대소문자 상관 없음) 외에 다른 걸 입력한 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "개념 강화 문제 추가 실패: 언어 종류를 java/python/c(대소문자 상관 없음) 외에 다른 걸 입력한 경우")
                            }
                            "E019" -> { // 로드맵 종류(type)를 algorithm, language(대소문자 상관 없음) 외에 다른 걸 입력할 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "개념 강화 문제 추가 실패: 로드맵 종류(type)를 algorithm, language(대소문자 상관 없음) 외에 다른 걸 입력할 경우")
                            }
                            "E020" -> { // 존재하지 않는 알고리즘 입력 시
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "개념 강화 문제 추가 실패: 존재하지 않는 알고리즘 입력 시")
                            }
                            else -> Log.d(TAG, "개념 강화 문제 추가 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }
}