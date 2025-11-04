package com.example.roadcode.data.repository

import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.repository.ResponseHandler.handleResponse
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class RoadmapRepository @Inject constructor(private val tokenRepository: TokenRepository) {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 로드맵 생성 */
    suspend fun createRoadmap(request: RoadmapDTO.CreateRequest) =
        handleResponse { jsonService.createRoadmap(tokenRepository.getBearerToken(), request) }

    /* 로드맵 정보 조회 */
    suspend fun getRoadmap(request: Long) =
        handleResponse { jsonService.getRoadmap(tokenRepository.getBearerToken(), request) }

    /* 로드맵 문제 목록 조회 */
    suspend fun getRoadmapProblems(request: Long) =
        handleResponse { jsonService.getRoadmapProblems(tokenRepository.getBearerToken(), request) }

    /* 문제 정보 조회 */
    suspend fun getProblem(request: Long) =
        handleResponse { jsonService.getProblem(tokenRepository.getBearerToken(), request) }

    /* 회원 로드맵 목록 조회 */
    suspend fun getRoadmaps(request: List<String>?) =
        handleResponse { jsonService.getRoadmaps(tokenRepository.getBearerToken(), request) }

    /* 로드맵 포기 */
    suspend fun giveUpRoadmap(request: Long) =
        handleResponse { jsonService.giveUpRoadmap(tokenRepository.getBearerToken(), request) }
}