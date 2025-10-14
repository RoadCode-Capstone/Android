package com.example.roadcode.data.repository

import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.data.model.ResponseUtilDTO
import com.example.roadcode.data.model.UserDTO
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class UserRepository @Inject constructor() {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)
    private val token: String = "Bearer fixed-test-token"

    /* 회원 정보 조회 */
    suspend fun getUserInfo(): Flow<Result<UserDTO.GetUserInfoResponse>> = flow {
        val response = jsonService.getUserInfo(token)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val userInfo = body.data!!
            emit(Result.success(userInfo))
        } else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }

    /* 회원 정보 수정 */
    suspend fun editUserInfo(request: UserDTO.EditUserInfoRequest): Flow<Result<String>> = flow {
        val response = jsonService.editUserInfo(token, request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val message = body.message!!
            emit(Result.success(message))
        } else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }

    /* 닉네임 중복 체크 */
    suspend fun checkNickname(request: String): Flow<Result<Boolean>> = flow {
        val response = jsonService.checkNickname(request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val duplicated = body.data!!.duplicated
            emit(Result.success(duplicated))
        } else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }

    /* 비밀번호 재확인 */
    suspend fun verifyPassword(request: UserDTO.VerifyPasswordRequest): Flow<Result<String>> = flow {
        val response = jsonService.verifyPassword(token, request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val codeMessage = "${response.code()}:${body.message!!}"
            emit(Result.success(codeMessage))
        }
        else if (response.code() == 400) {  // HTTP 코드 400
            val errorJson = response.errorBody()?.string()
            val errorBody = runCatching {
                Gson().fromJson(
                    errorJson,
                    ResponseUtilDTO.Response::class.java
                ) as ResponseUtilDTO.Response<*>
            }.getOrNull()

            val codeMessage = "${response.code()}:${errorBody?.message}"
            emit(Result.success(codeMessage))
        }
        else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }

    /* 비밀번호 변경 */
    suspend fun editPassword(request: UserDTO.EditPasswordRequest): Flow<Result<String>> = flow {
        val response = jsonService.editPassword(token, request)

        if (response.isSuccessful) {
            val body = response.body()

            if (body?.code != "SUCCESS") {
                throw Exception("${body?.code.toString()}: ${body?.message.toString()}")
            }

            val codeMessage = "${response.code()}:${body.message!!}"
            emit(Result.success(codeMessage))
        }
        else if (response.code() == 400) {  // HTTP 코드 400
            val errorJson = response.errorBody()?.string()
            val errorBody = runCatching {
                Gson().fromJson(
                    errorJson,
                    ResponseUtilDTO.Response::class.java
                ) as ResponseUtilDTO.Response<*>
            }.getOrNull()

            val codeMessage = "${response.code()}:${errorBody?.message}"
            emit(Result.success(codeMessage))
        }
        else {
            throw HttpException(response)
        }
    }.catch { e ->
        emit(Result.failure(e))
    }
}