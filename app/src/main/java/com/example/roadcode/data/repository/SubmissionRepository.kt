package com.example.roadcode.data.repository

import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.data.repository.ResponseHandler.handleResponse
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class SubmissionRepository @Inject constructor(private val tokenRepository: TokenRepository) {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 문제 정보 조회 */
    suspend fun getProblem(request: Long) =
        handleResponse { jsonService.getProblem(tokenRepository.getBearerToken(), request) }

    /* 풀이 제출 */
    suspend fun submitSolution(problemId: Long, request: SubmissionDTO.SubmitSolutionRequest) =
        handleResponse { jsonService.submitSolution(tokenRepository.getBearerToken(), problemId, request) }
}