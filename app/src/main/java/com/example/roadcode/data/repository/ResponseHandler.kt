package com.example.roadcode.data.repository

import com.example.roadcode.data.model.ResponseUtilDTO
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

object ResponseHandler {
    suspend fun <T> handleResponse(
        call: suspend () -> Response<ResponseUtilDTO.Response<T>>
    ): Flow<Result<ResponseUtilDTO.Response<T>>> = flow {
        val response = call()

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(Result.success(body))
            } else {
                emit(Result.failure(Exception("Response body is null")))
            }
        } else {
            val errorString = response.errorBody()?.string()

            val parsedErrorBody = Gson().fromJson(
                errorString,
                ResponseUtilDTO.Response::class.java
            ) as ResponseUtilDTO.Response<T>

            emit(Result.success(parsedErrorBody))
        }
    }.catch { e ->
        emit(Result.failure(e))
    }
}
