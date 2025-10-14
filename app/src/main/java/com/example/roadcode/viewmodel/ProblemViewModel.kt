package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.data.repository.RoadmapRepository
import com.example.roadcode.data.repository.SubmissionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProblemViewModel @Inject constructor(private val repository: SubmissionRepository) : ViewModel() {
    companion object {
        private const val TAG = "ProblemViewModel"
    }

    private val _problemId = MutableStateFlow<Long>(0)    // 문제 아이디
    val problemId = _problemId.asStateFlow()
    private val _problemInfo = MutableStateFlow<List<String>>(emptyList()) // 문제 정보 (제목, 설명, 입력 설명, 출력 설명, 시간제한, 메모리제한)
    val problemInfo = _problemInfo.asStateFlow()
    private val _code = MutableStateFlow("") // 작성한 코드
    val code = _code.asStateFlow()
    private val _result = MutableStateFlow("")  // 채점 결과
    val result = _result.asStateFlow()

    /* 코드 입력 이벤트 */
    fun updateCode(input: String) {
        _code.value = input
    }

    /* 문제 정보 조회 함수 */
    fun getProblem(problemId: Long) {
        viewModelScope.launch {
            val request = problemId

            repository.getProblem(request).collect() { result ->
                result
                    .onSuccess { problemInfo ->
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
                        Log.d(TAG, "문제 정보: ${problemInfo}")
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 풀이 제출 함수 */
    fun submitSolution(language: String) {
        viewModelScope.launch {
            val request = SubmissionDTO.SubmitSolutionRequest(language, code.value)

            repository.submitSolution(problemId.value, request).collect() { result ->
                result
                    .onSuccess { result ->
                        _result.value = if (result.allPassed) "풀이 성공!" else "풀이 실패"
                        Log.d(TAG, "풀이 제출 결과: ${result}")
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 결과 초기화 */
    fun clearResult() {
        _result.value = ""
    }
}