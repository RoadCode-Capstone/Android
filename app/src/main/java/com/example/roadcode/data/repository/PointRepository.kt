package com.example.roadcode.data.repository

import com.example.roadcode.data.model.PointDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.repository.ResponseHandler.handleResponse
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class PointRepository @Inject constructor(private val tokenRepository: TokenRepository) {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 순위 조회 */
    suspend fun getRanking(start: String, end: String) =
        handleResponse { jsonService.getRanking(tokenRepository.getBearerToken(), start, end) }

    /* 포인트 내역 조회 */
    suspend fun getPointsByDate(start: String, end: String) =
        handleResponse { jsonService.getPointsByDate(tokenRepository.getBearerToken(), start, end) }
}