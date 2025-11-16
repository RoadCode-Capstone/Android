package com.example.roadcode.data.repository

import android.content.Context
import android.util.Log
import com.example.roadcode.data.repository.ResponseHandler.handleResponse
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AttendanceRepository @Inject constructor(private val tokenRepository: TokenRepository) {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 출석 체크 */
    suspend fun checkAttendance() =
        handleResponse {
            jsonService.checkAttendance(tokenRepository.getBearerToken())
        }
}