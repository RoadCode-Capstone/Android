package com.example.roadcode.data.repository

import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class LevelTestRepository @Inject constructor() {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 레벨 테스트 생성 */
    suspend fun createLevelTest(request: LevelTestDTO.createRequest): Flow<Result<List<Long>>> = flow {
        try {
            val response = jsonService.createLevelTest("Bearer fixed-test-token", request)
            if (response.isSuccessful) {
                val levelTestIds = response.body()?.data?.get("problemIds") as List<Long>
                emit(Result.success(levelTestIds))
            } else {
                throw HttpException(response)
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}