package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val repository: AttendanceRepository) : ViewModel() {
    companion object {
        private val TAG = "AttendanceViewModel"
    }

    init {
        checkAttendance()
    }

    /* 출석 체크 */
    fun checkAttendance() {
        viewModelScope.launch {
            repository.checkAttendance().collect() { result ->
                result
                    .onSuccess { message ->
                        Log.d(TAG, "출석 체크: ${message}")
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }
}