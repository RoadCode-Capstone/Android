package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.LevelTestDTO
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
    val levelTestIds = _levelTestIds.asStateFlow()
    private val _levelTestProblems = MutableStateFlow<List<LevelTestDTO.getResponse>>(emptyList())  // 레벨 테스트 문제 정보 리스트
    val levelTestProblems = _levelTestProblems.asStateFlow()
    private val _codes = MutableStateFlow<Map<Int, String>>(emptyMap())    // 작성한 코드 맵
    val codes = _codes.asStateFlow()
    private val _levelTestResults = MutableStateFlow<LevelTestDTO.submitResponse?>(null)    // 레벨 테스트 결과
    val levelTestResults = _levelTestResults.asStateFlow()

    /* 레벨 테스트 생성 함수 */
    fun createLevelTest(request: LevelTestDTO.createRequest) {
        viewModelScope.launch {
            repository.createLevelTest(request).collect() { result ->
                result
                    .onSuccess { levelTestIds ->
                        _levelTestIds.value = levelTestIds
                        Log.d(TAG, "레벨 테스트 목록: ${_levelTestIds.value}")
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 레벨 테스트 문제 조회 함수 */
    fun getLevelTestProblems(request: List<Long>) {
        viewModelScope.launch {
            repository.getLevelTestProblems(request).collect() { result ->
                result
                    .onSuccess { levelTestProblems ->
                        _levelTestProblems.value = levelTestProblems
                        Log.d(TAG, "레벨 테스트 목록: ${_levelTestProblems.value}")
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 작성한 코드 저장 함수 */
    fun updateCode(problemIdx: Int, code: String) {
        _codes.value = _codes.value.toMutableMap().apply {
            this[problemIdx] = code
        }
        Log.d(TAG, "코드 저장 ${_codes.value[problemIdx]}")
    }

    /* 레벨 테스트 제출 함수 */
    fun submitLevelTest(request: List<LevelTestDTO.submitRequest>) {
        viewModelScope.launch {
            repository.submitLevelTest(request).collect() { result ->
                result
                    .onSuccess { levelTestResults ->
                        _levelTestResults.value = levelTestResults
                        Log.d(TAG, "레벨 테스트 결과: ${_levelTestResults.value}")
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }
}