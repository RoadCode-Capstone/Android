package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.repository.RoadmapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/* 학습 계획 데이터 클래스 */
data class Plan(
    val selectedLanguage: String? = null,
    val selectedType: String? = null,
    val selectedAlgorithm: String? = null,
    val selectedGoal: Int? = null
)

@HiltViewModel
class RoadmapPlanViewModel @Inject constructor(private val repository: RoadmapRepository) : ViewModel() {
    companion object {
        private const val TAG = "RoadmapPlanViewModel"
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _plan = MutableStateFlow(Plan())
    val plan = _plan.asStateFlow()

    /* 로드맵 유형 초기화 함수 */
    fun initPlan() {
        _plan.value = Plan()
    }

    /* 사용 언어 설정 함수 */
    fun setSelectedLanguage(language: String?) {
        _plan.value = _plan.value.copy(selectedLanguage = language)
        Log.d(TAG, "학습 계획 - 언어 변경: ${language}")
    }

    /* 로드맵 유형 설정 함수 */
    fun setSelectedType(type: String?) {
        _plan.value = _plan.value.copy(selectedType = type)
        Log.d(TAG, "학습 계획 - 유형 변경: ${type}")
    }

    /* 학습할 알고리즘 설정 함수 */
    fun setSelectedAlgorithm(algorithm: String?) {
        _plan.value = _plan.value.copy(selectedAlgorithm = algorithm)
        Log.d(TAG, "학습 계획 - 알고리즘 변경: ${algorithm}")
    }

    /* 일일 학습 목표 설정 함수 */
    fun setSelectedGoal(goal: Int?) {
        _plan.value = _plan.value.copy(selectedGoal = goal)
        Log.d(TAG, "학습 계획 - 일일 학습 목표 변경: ${goal}")
    }

    /* 로드맵 생성 함수 */
    fun createRoadmap(result: Int, completed: (Long) -> Unit) {
        val request = RoadmapDTO.CreateRequest(plan.value.selectedType!!, plan.value.selectedLanguage!!, plan.value.selectedAlgorithm, plan.value.selectedGoal!!, result)
        Log.d(TAG, "로드맵 생성 요청\n${request}")
        viewModelScope.launch {
            _isLoading.value = true
            repository.createRoadmap(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val roadmapId = body.data!!.id
                                completed(roadmapId)

                                Log.d(TAG, "로드맵 생성 성공\n로드맵 아이디: ${roadmapId}")
                            }
                            "E015" -> { // 언어 종류를 java/python/c(대소문자 상관 없음) 외에 다른 걸 입력한 경우
                                Log.d(TAG, "로드맵 생성 실패: 언어 종류를 java/python/c(대소문자 상관 없음) 외에 다른 걸 입력한 경우")
                            }
                            "E019" -> { // 로드맵 종류(type)를 algorithm, language(대소문자 상관 없음) 외에 다른 걸 입력할 경우
                                Log.d(TAG, "로드맵 생성 실패: 로드맵 종류(type)를 algorithm, language(대소문자 상관 없음) 외에 다른 걸 입력할 경우")
                            }
                            "E020" -> { // 존재하지 않는 알고리즘 입력할 경우
                                Log.d(TAG, "로드맵 생성 실패: 존재하지 않는 알고리즘 입력할 경우")
                            }
                            else -> Log.d(TAG, "로드맵 생성 실패: 알 수 없는 오류")
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