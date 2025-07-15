package com.example.roadcode.data.repository

import android.util.Log
import com.example.roadcode.data.model.ResponseUtilDTO
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.create
import javax.inject.Inject

class TagRepository @Inject constructor() {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 태그 목록 조회 */
    suspend fun fetchTags(): Flow<Result<List<String>>> = flow {
        try {
            val response = jsonService.getTags()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.code != "SUCCESS") {
                    emit(Result.failure(Exception("${body?.code.toString()}: ${body?.message.toString()}")))
                }

                val tags = body!!.data!!.tags
                emit(Result.success(tags))
            } else {
                emit(Result.failure(HttpException(response)))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}