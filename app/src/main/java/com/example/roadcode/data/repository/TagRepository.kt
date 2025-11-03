package com.example.roadcode.data.repository

import android.util.Log
import com.example.roadcode.data.model.ResponseUtilDTO
import com.example.roadcode.data.repository.ResponseHandler.handleResponse
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.create
import javax.inject.Inject

class TagRepository @Inject constructor() {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 태그 목록 조회 */
    suspend fun fetchTags() =
        handleResponse { jsonService.getTags() }
}