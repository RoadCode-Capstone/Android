package com.example.roadcode.data.repository

import android.util.Log
import com.example.roadcode.data.model.ResponseUtilDTO
import com.example.roadcode.data.model.UserDTO
import com.example.roadcode.data.repository.ResponseHandler.handleResponse
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val tokenRepository: TokenRepository) {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 회원 정보 조회 */
    suspend fun getUserInfo() =
        handleResponse { jsonService.getUserInfo(tokenRepository.getBearerToken()) }

    /* 회원 정보 수정 */
    suspend fun editUserInfo(request: UserDTO.EditUserInfoRequest) =
        handleResponse { jsonService.editUserInfo(tokenRepository.getBearerToken(), request) }

    /* 닉네임 중복 체크 */
    suspend fun checkDuplicatedNickname(request: String) =
        handleResponse { jsonService.checkDuplicatedNickname(request) }

    /* 비밀번호 재확인 */
    suspend fun verifyPassword(request: UserDTO.VerifyPasswordRequest) =
        handleResponse { jsonService.verifyPassword(tokenRepository.getBearerToken(), request) }

    /* 비밀번호 변경 */
    suspend fun editPassword(request: UserDTO.EditPasswordRequest) =
        handleResponse { jsonService.editPassword(tokenRepository.getBearerToken(), request) }

    /* 회원 탈퇴 */
    suspend fun deleteMember(request: UserDTO.DeleteMemberRequest) =
        handleResponse { jsonService.deleteMember(tokenRepository.getBearerToken(), request) }

    /* 이메일 로그인 */
    suspend fun emailLogin(request: UserDTO.EmailLoginRequest) =
        handleResponse { jsonService.emailLogin(request) }

    /* 이메일 회원가입 */
    suspend fun emailRegister(request: UserDTO.EmailRegisterRequest) =
        handleResponse { jsonService.emailRegister(request) }

    /* 이메일로 인증코드 전송 - 회원가입 */
    suspend fun verifyEmailRegister(request: UserDTO.VerifyEmailRequest) =
        handleResponse { jsonService.verifyEmailRegister(request) }

    /* 이메일로 인증코드 전송 - 비밀번호 재설정 */
    suspend fun verifyEmailPassword(request: UserDTO.VerifyEmailRequest) =
        handleResponse { jsonService.verifyEmailPassword(request) }

    /* 인증코드 확인 */
    suspend fun verifyCode(request: UserDTO.VerifyCodeRequest) =
        handleResponse { jsonService.verifyCode(request) }

    /* 이메일 중복 체크 */
    suspend fun checkDuplicatedEmail(request: String) =
        handleResponse { jsonService.checkDuplicatedEmail(request) }

    /* 로그아웃 */
    suspend fun logout() =
        handleResponse { jsonService.logout(tokenRepository.getBearerToken()) }
}