package com.example.roadcode.data.repository

import com.example.roadcode.data.model.LevelTestDTO
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

class LevelTestRepository @Inject constructor(private val tokenRepository: TokenRepository) {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 레벨 테스트 생성 */
    suspend fun createLevelTest(request: LevelTestDTO.createRequest) =
        handleResponse { jsonService.createLevelTest(tokenRepository.getBearerToken(), request) }

    /* 레벨 테스트 문제 조회 */
    suspend fun getLevelTestProblems(request: List<Long>) =
        handleResponse { jsonService.getLevelTestProblems(tokenRepository.getBearerToken(), request) }

    /* 레벨 테스트 제출 */
    suspend fun submitLevelTest(request: LevelTestDTO.submitRequest) =
        handleResponse { jsonService.submitLevelTest(tokenRepository.getBearerToken(), request) }
}