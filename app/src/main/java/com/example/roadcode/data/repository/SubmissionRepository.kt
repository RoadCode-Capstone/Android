package com.example.roadcode.data.repository

import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class SubmissionRepository @Inject constructor() {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)
    private val token: String = "Bearer fixed-test-token"

    /* 문제 정보 조회 */
    suspend fun getProblem(request: Long): Flow<Result<ProblemDTO.ProblemData>> = flow {
        val response = jsonService.getProblem(token, request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val problemInfo = body!!.data!!
            emit(Result.success(problemInfo))
        } else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }

    /* 풀이 제출 */
    suspend fun submitSolution(problemId: Long, request: SubmissionDTO.SubmitSolutionRequest): Flow<Result<SubmissionDTO.SubmitSolutionResponse>> = flow {
        val response = jsonService.submitSolution(token, problemId, request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val submitResult = body.data!!
            emit(Result.success(submitResult))
        } else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }
}