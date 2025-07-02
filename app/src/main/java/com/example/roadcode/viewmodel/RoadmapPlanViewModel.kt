package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoadmapPlanViewModel @Inject constructor() : ViewModel() {
    // 학습 계획 데이터 클래스
    data class Plan(
        val selectedLanguage: String? = null,
        val selectedType: String? = null,
        val selectedAlgorithm: String? = null,
        val selectedGoal: Int? = null
    )

    private val _plan = MutableStateFlow(Plan())
    val plan = _plan.asStateFlow()

    fun setSelectedLanguage(language: String?) {
        _plan.value = _plan.value.copy(selectedLanguage = language)
        Log.d("언어", "언어 수정 ${_plan.value.selectedLanguage}")
    }

    fun setSelectedType(type: String?) {
        _plan.value = _plan.value.copy(selectedType = type)
    }

    fun setSelectedAlgorithm(algorithm: String?) {
        _plan.value = _plan.value.copy(selectedAlgorithm = algorithm)
    }

    fun setSelectedGoal(goal: Int?) {
        _plan.value = _plan.value.copy(selectedGoal = goal)
    }
}