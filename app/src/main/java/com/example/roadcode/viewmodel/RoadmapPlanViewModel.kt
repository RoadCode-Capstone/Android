package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoadmapPlanViewModel @Inject constructor() : ViewModel() {
    companion object {
        private const val TAG = "RoadmapPlanViewModel"
    }

    /* 학습 계획 데이터 클래스 */
    data class Plan(
        val selectedLanguage: String? = null,
        val selectedType: String? = null,
        val selectedAlgorithm: String? = null,
        val selectedGoal: Int? = null
    )

    private val _plan = MutableStateFlow(Plan())
    val plan = _plan.asStateFlow()

    /* 사용 언어 설정 함수 */
    fun setSelectedLanguage(language: String?) {
        _plan.value = _plan.value.copy(selectedLanguage = language)
        Log.d(TAG, "학습 계획 - 언어 변경: ${_plan.value.selectedLanguage}")
    }

    /* 로드맵 유형 설정 함수 */
    fun setSelectedType(type: String?) {
        _plan.value = _plan.value.copy(selectedType = type)
        Log.d(TAG, "학습 계획 - 유형 변경: ${_plan.value.selectedType}")
    }

    /* 학습할 알고리즘 설정 함수 */
    fun setSelectedAlgorithm(algorithm: String?) {
        _plan.value = _plan.value.copy(selectedAlgorithm = algorithm)
        Log.d(TAG, "학습 계획 - 알고리즘 변경: ${_plan.value.selectedAlgorithm}")
    }

    /* 일일 학습 목표 설정 함수 */
    fun setSelectedGoal(goal: Int?) {
        _plan.value = _plan.value.copy(selectedGoal = goal)
        Log.d(TAG, "학습 계획 - 일일 학습 목표 변경: ${_plan.value.selectedGoal}")
    }
}