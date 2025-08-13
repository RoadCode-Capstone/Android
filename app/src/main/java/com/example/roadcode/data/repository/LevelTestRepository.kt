package com.example.roadcode.data.repository

import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class LevelTestRepository @Inject constructor() {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)
    private val token: String = "Bearer fixed-test-token"

    /* 레벨 테스트 생성 */
    suspend fun createLevelTest(request: LevelTestDTO.createRequest): Flow<Result<List<Long>>> = flow {
        val response = jsonService.createLevelTest(token, request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val levelTestIds = body!!.data!!.problemIds
            emit(Result.success(levelTestIds))
        } else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }

    /* 레벨 테스트 문제 조회 */
    suspend fun getLevelTestProblems(request: List<Long>): Flow<Result<List<ProblemDTO.ProblemData>>> = flow {
        val response = jsonService.getLevelTestProblems(token, request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val levelTestProblems = body!!.data!!.problems
            emit(Result.success(levelTestProblems))
        } else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }


    /* 레벨 테스트 제출 */
    suspend fun submitLevelTest(request: LevelTestDTO.submitRequest): Flow<Result<LevelTestDTO.submitResponse>> = flow {
        val response = jsonService.submitLevelTest(token, request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val levelTestIds = body!!.data!!
            emit(Result.success(levelTestIds))
        } else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }
}