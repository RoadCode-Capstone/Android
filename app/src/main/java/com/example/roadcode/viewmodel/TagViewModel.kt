package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.repository.TagRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(private val repository: TagRepository) : ViewModel() {
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
                    .onSuccess { tagsMap ->
                        _tags.value = tagsMap
                        Log.d("태그 목록", "태그 목록: ${_tags.value.toString()}")
                    }.onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }
}