package com.example.roadcode.data.repository

import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class RoadmapRepository @Inject constructor() {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)
    private val token: String = "Bearer fixed-test-token"

    /* 로드맵 생성 */
    suspend fun createRoadmap(request: RoadmapDTO.createRequest): Flow<Result<Long>> = flow {
        try {
            val response = jsonService.createRoadmap(token, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.code != "SUCCESS") {
                    emit(Result.failure(Exception("${body?.code.toString()}: ${body?.message.toString()}")))
                }

                val roadmapId = body!!.data!!.id
                emit(Result.success(roadmapId))
            } else {
                emit(Result.failure(HttpException(response)))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}