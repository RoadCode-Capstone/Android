package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.data.repository.LevelTestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelTestViewModel @Inject constructor(private val repository: LevelTestRepository) : ViewModel() {
    companion object {
        private const val TAG = "LevelTestViewModel"
    }

    private val _levelTestIds = MutableStateFlow<List<Long>>(emptyList())   // 레벨 테스트 문제 아이디 리스트
//    private val _levelTestIds = MutableStateFlow<List<Long>>(listOf(584, 2000, 237, 62, 70))
    val levelTestIds = _levelTestIds.asStateFlow()
    private val _levelTestProblems = MutableStateFlow<List<ProblemDTO.ProblemData>>(emptyList())  // 레벨 테스트 문제 정보 리스트
    val levelTestProblems = _levelTestProblems.asStateFlow()
    private val _problemInfos = MutableStateFlow<List<List<String>>>(emptyList())   // 문제 정보 리스트 (제목, 설명, 입력 설명, 출력 설명, 시간제한, 메모리제한)
    val problemInfos = _problemInfos.asStateFlow()
    private val _codes = MutableStateFlow<Map<Int, String>>((0..4).associateWith { "" })    // 작성한 코드 맵
    val codes = _codes.asStateFlow()
    private val _levelTestResults = MutableStateFlow<LevelTestDTO.submitResponse?>(null)    // 레벨 테스트 결과
    val levelTestResults = _levelTestResults.asStateFlow()

    /* 작성한 코드 저장 함수 */
    fun updateCode(problemIdx: Int, code: String) {
        _codes.value = _codes.value.toMutableMap().apply {
            this[problemIdx] = code
        }
        Log.d(TAG, "코드 저장 ${codes.value[problemIdx]}")
    }

    /* 문제 정보 리스트 리턴 함수 */
    fun getProblemInfos() {
        val infos = levelTestProblems.value.map { problem ->
            listOf(
                problem.name,
                problem.description,
                problem.inputDescription,
                problem.outputDescription,
                problem.timeLimit,
                problem.memoryLimit
            )
        }

        _problemInfos.value = infos
    }

    /* 레벨 테스트 측정 결과 리턴 함수 */
    fun getResult(): Int {
        var answer = 0

        levelTestResults.value!!.result.forEachIndexed { idx, result ->
            if (result) {
                answer += levelTestProblems.value[idx].rating
            }
        }

        return if (answer != 0) answer / levelTestResults.value!!.passedCount else 0
    }

    /* 레벨 테스트 생성 함수 */
    fun createLevelTest(plans: LevelTestDTO.createRequest) {
        viewModelScope.launch {
            repository.createLevelTest(plans).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val levelTestIds = body.data!!.problemIds
                                _levelTestIds.value = levelTestIds

                                getLevelTestProblems(levelTestIds)  // 레벨 테스트 문제 조회

                                Log.d(TAG, "레벨 테스트 생성 성공\n레벨 테스트 아이디 목록: ${levelTestIds}")
                            }
                            "E015" -> { // 언어 종류를 java/python/c(대소문자 상관 없음) 외에 다른 걸 입력한 경우
                                Log.d(TAG, "레벨 테스트 생성 실패: 언어 종류를 java/python/c(대소문자 상관 없음) 외에 다른 걸 입력한 경우")
                            }
                            "E019" -> { // 로드맵 종류(type)를 algorithm, language(대소문자 상관 없음) 외에 다른 걸 입력할 경우
                                Log.d(TAG, "레벨 테스트 생성 실패: 로드맵 종류(type)를 algorithm, language(대소문자 상관 없음) 외에 다른 걸 입력할 경우")
                            }
                            "E020" -> { // 존재하지 않는 알고리즘 입력할 경우
                                Log.d(TAG, "레벨 테스트 생성 실패: 존재하지 않는 알고리즘 입력할 경우")
                            }
                            else -> Log.d(TAG, "레벨 테스트 생성 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }

    /* 레벨 테스트 문제 조회 함수 */
    fun getLevelTestProblems(problemIds: List<Long>) {
        viewModelScope.launch {
            repository.getLevelTestProblems(problemIds).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val levelTestProblems = body.data!!.problems
                                _levelTestProblems.value = levelTestProblems

                                getProblemInfos()  // 문제 정보 리스트 저장

                                Log.d(TAG, "레벨 테스트 문제 조회 성공\n${levelTestProblems}")
                            }
                            else -> Log.d(TAG, "레벨 테스트 문제 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }

    /* 레벨 테스트 제출 함수 */
    fun submitLevelTest(language: String) {
        val submissions = levelTestIds.value.mapIndexed { index, id ->
            SubmissionDTO.SubmissionData(
                problemId = id,
                language = language,
                sourceCode = codes.value[index] ?: ""
            )
        }

        val request = LevelTestDTO.submitRequest(submissions)

        viewModelScope.launch {
            repository.submitLevelTest(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val levelTestResults = body.data!!
                                _levelTestResults.value = levelTestResults

                                Log.d(TAG, "레벨 테스트 제출 성공\n${levelTestResults}")
                            }
                            else -> Log.d(TAG, "레벨 테스트 제출 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }
}