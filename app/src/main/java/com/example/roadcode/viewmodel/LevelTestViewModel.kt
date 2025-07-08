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

    private val _levelTestIds = MutableStateFlow<List<Long>>(emptyList())
    val levelTestIds = _levelTestIds.asStateFlow()

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
}