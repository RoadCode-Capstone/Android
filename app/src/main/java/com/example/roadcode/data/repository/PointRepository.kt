package com.example.roadcode.data.repository

import com.example.roadcode.data.repository.ResponseHandler.handleResponse
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import javax.inject.Inject

class PointRepository @Inject constructor(private val tokenRepository: TokenRepository) {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 순위 조회 */
    suspend fun getRanking(start: String, end: String) =
        handleResponse { jsonService.getRanking(tokenRepository.getBearerToken(), start, end) }

    /* 날짜별 포인트 내역 조회 */
    suspend fun getPointsByDate(start: String, end: String) =
        handleResponse { jsonService.getPointsByDate(tokenRepository.getBearerToken(), start, end) }

    /* 종류별 포인트 내역 조회 */
    suspend fun getPointsByType(start: String, end: String) =
        handleResponse { jsonService.getPointsByType(tokenRepository.getBearerToken(), start, end) }
}