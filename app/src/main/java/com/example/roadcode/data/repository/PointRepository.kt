package com.example.roadcode.data.repository

import com.example.roadcode.data.model.PointDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class PointRepository @Inject constructor() {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)
    private val token: String = "Bearer fixed-test-token"

    /* 순위 조회 */
    suspend fun getRanking(start: String, end: String): Flow<Result<PointDTO.GetRankingResponse>> = flow {
        val response = jsonService.getRanking(token, start, end)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val rankData = body!!.data!!
            emit(Result.success(rankData))
        } else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }
}