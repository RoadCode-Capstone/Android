package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoadmapPlanViewModel @Inject constructor() : ViewModel() {
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
        Log.d("언어", "언어 수정 ${_plan.value.selectedLanguage}")
    }

    /* 로드맵 유형 설정 함수 */
    fun setSelectedType(type: String?) {
        _plan.value = _plan.value.copy(selectedType = type)
    }

    /* 학습할 알고리즘 설정 함수 */
    fun setSelectedAlgorithm(algorithm: String?) {
        _plan.value = _plan.value.copy(selectedAlgorithm = algorithm)
    }

    /* 일일 학습 목표 설정 함수 */
    fun setSelectedGoal(goal: Int?) {
        _plan.value = _plan.value.copy(selectedGoal = goal)
    }
}