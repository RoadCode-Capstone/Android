package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.data.repository.RoadmapRepository
import com.example.roadcode.data.repository.SubmissionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProblemViewModel @Inject constructor(private val repository: SubmissionRepository) : ViewModel() {
    companion object {
        private const val TAG = "ProblemViewModel"
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _problemId = MutableStateFlow<Long>(0)    // 문제 아이디
    val problemId = _problemId.asStateFlow()
    private val _problemInfo = MutableStateFlow<List<String>>(emptyList()) // 문제 정보 (제목, 설명, 입력 설명, 출력 설명, 시간제한, 메모리제한)
    val problemInfo = _problemInfo.asStateFlow()
    private val _code = MutableStateFlow("") // 작성한 코드
    val code = _code.asStateFlow()
    private val _isSuccess = MutableStateFlow<Boolean?>(null)   // 성공 여부
    val isSuccess = _isSuccess.asStateFlow()

    /* 성공 여부 초기화 */
    fun resetIsSuccess() {
        _isSuccess.value = null
    }

    /* 코드 입력 이벤트 */
    fun updateCode(input: String) {
        _code.value = input
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
                                val info = listOf(
                                    problemInfo.name,
                                    problemInfo.description,
                                    problemInfo.inputDescription,
                                    problemInfo.outputDescription,
                                    problemInfo.timeLimit,
                                    problemInfo.memoryLimit
                                )
                                _problemInfo.value = info
                                _problemId.value = problemInfo.problemId

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

    /* 풀이 제출 함수 */
    fun submitSolution(roadmapInfo: RoadmapDTO.RoadmapData) {
        val request = SubmissionDTO.SubmitSolutionRequest(
            roadmapInfo.roadmapId,
            roadmapInfo.currentProblem.roadmapProblemId,
            roadmapInfo.language,
            code.value
        )

        viewModelScope.launch {
            _isLoading.value = true
            repository.submitSolution(problemId.value, request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val submissionResult = body.data!!
                                _isSuccess.value = if (submissionResult.allPassed) true else false

                                Log.d(TAG, "풀이 제출 성공\n${submissionResult}")
                            }
                            "E001" -> { // 사용자를 찾을 수 없음
                                Log.d(TAG, "풀이 제출 실패: 사용자를 찾을 수 없음")
                            }
                            "E002" -> { // 토큰 없음
                                Log.d(TAG, "풀이 제출 실패: 토큰 없음")
                            }
                            "E013" -> { // 테스트 케이스가 없는 경우(문제 id가 잘못된 경우)
                                Log.d(TAG, "풀이 제출 실패: 테스트 케이스가 없는 경우(문제 id가 잘못된 경우)")
                            }
                            "E015" -> { // 없는 언어를 입력한 경우(java/c/python 외 언어)
                                Log.d(TAG, "풀이 제출 실패: 없는 언어를 입력한 경우(java/c/python 외 언어)")
                            }
                            else -> Log.d(TAG, "풀이 제출 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
            _isLoading.value = false
        }
    }
}