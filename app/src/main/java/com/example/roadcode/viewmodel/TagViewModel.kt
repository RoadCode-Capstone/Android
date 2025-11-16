package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(private val repository: TagRepository) : ViewModel() {
    companion object {
        private const val TAG = "TagViewModel"
    }

    private val _tags = MutableStateFlow<List<String>>(emptyList())
    val tags = _tags.asStateFlow()

    init {
        fetchTags()
    }

    /* 태그 목록 조회 함수 */
    private fun fetchTags() {
        viewModelScope.launch {
            repository.fetchTags().collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val tagsMap = body.data!!.tags
                                _tags.value = tagsMap

                                Log.d(TAG, "태그 목록 조회 성공\n${tagsMap}")
                            }
                            else -> Log.d(TAG, "태그 목록 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }
}