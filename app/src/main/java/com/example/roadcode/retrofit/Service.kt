package com.example.roadcode.retrofit

import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.data.model.PointDTO
import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.ResponseUtilDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.data.model.TagDTO
import com.example.roadcode.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/* JSON 데이터 처리 */
interface JsonService {
    // 태그 목록 조회
    @GET("/api/v1/tags")
    suspend fun getTags(): Response<ResponseUtilDTO.Response<TagDTO.TagsResponse>>

    // 레벨 테스트 생성
    @POST("/api/v1/level-test")
    suspend fun createLevelTest(
        @Header("Authorization") token: String,
        @Body request: LevelTestDTO.createRequest
    ): Response<ResponseUtilDTO.Response<LevelTestDTO.createResponse>>

    // 레벨 테스트 문제 조회
    @GET("/api/v1/problems")
    suspend fun getLevelTestProblems(
        @Header("Authorization") token: String,
        @Query("ids") ids: List<Long>
    ): Response<ResponseUtilDTO.Response<LevelTestDTO.getResponse>>

    // 레벨 테스트 제출
    @POST("/api/v1/level-test/submissions")
    suspend fun submitLevelTest(
        @Header("Authorization") token: String,
        @Body request: LevelTestDTO.submitRequest
    ): Response<ResponseUtilDTO.Response<LevelTestDTO.submitResponse>>

    // 로드맵 생성
    @POST("/api/v1/roadmaps")
    suspend fun createRoadmap(
        @Header("Authorization") token: String,
        @Body request: RoadmapDTO.CreateRequest
    ): Response<ResponseUtilDTO.Response<RoadmapDTO.CreateResponse>>

    // 로드맵 정보 조회
    @GET("/api/v1/roadmaps/{roadmapId}")
    suspend fun getRoadmap(
        @Header("Authorization") token: String,
        @Path("roadmapId") roadmapId: Long
    ): Response<ResponseUtilDTO.Response<RoadmapDTO.RoadmapData>>

    // 로드맵 문제 목록 조회
    @GET("api/v1/roadmaps/{roadmapId}/problems")
    suspend fun getRoadmapProblems(
        @Header("Authorization") token: String,
        @Path("roadmapId") roadmapId: Long
    ): Response<ResponseUtilDTO.Response<RoadmapDTO.GetProblemsResponse>>

    // 문제 정보 조회
    @GET("/api/v1/problems/{problemId}")
    suspend fun getProblem(
        @Header("Authorization") token: String,
        @Path("problemId") problemId: Long
    ): Response<ResponseUtilDTO.Response<ProblemDTO.ProblemData>>

    // 로드맵 포기
    @POST("/api/v1/roadmaps/{roadmapId}/give-up")
    suspend fun giveUpRoadmap(
        @Header("Authorization") token: String,
        @Path("roadmapId") roadmapId: Long
    ): Response<ResponseUtilDTO.Response<Nothing>>

    // 회원 로드맵 목록 조회
    @GET("/api/v1/roadmaps/my")
    suspend fun getRoadmaps(
        @Header("Authorization") token: String,
        @Query("statusList") statusList: List<String>
    ): Response<ResponseUtilDTO.Response<RoadmapDTO.GetRoadmapsResponse>>

    // 순위 조회
    @GET("api/v1/points/ranking")
    suspend fun getRanking(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Response<ResponseUtilDTO.Response<PointDTO.GetRankingResponse>>

    // 날짜별 포인트 내역 조회
    @GET("/api/v1/points/my?groupBy=DATE")
    suspend fun getPointsByDate(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Response<ResponseUtilDTO.Response<PointDTO.GetPointsByDateResponse>>

    // 회원 정보 조회
    @GET("/api/v1/member")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): Response<ResponseUtilDTO.Response<UserDTO.GetUserInfoResponse>>

    // 회원 정보 수정
    @PUT("/api/v1/member")
    suspend fun editUserInfo(
        @Header("Authorization") token: String,
        @Body request: UserDTO.EditUserInfoRequest
    ): Response<ResponseUtilDTO.Response<Nothing>>

    // 닉네임 중복 체크
    @GET("/api/v1/member/exists-nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String
    ): Response<ResponseUtilDTO.Response<UserDTO.CheckNicknameResponse>>

    // 비밀번호 재확인
    @POST("/api/v1/member/verify-password")
    suspend fun verifyPassword(
        @Header("Authorization") token: String,
        @Body request: UserDTO.VerifyPasswordRequest
    ): Response<ResponseUtilDTO.Response<Nothing>>

    // 비밀번호 변경
    @PUT("/api/v1/member/password")
    suspend fun editPassword(
        @Header("Authorization") token: String,
        @Body request: UserDTO.EditPasswordRequest
    ): Response<ResponseUtilDTO.Response<Nothing>>

    // 풀이 제출
    @POST("/api/v1/problems/{problemId}/submission")
    suspend fun submitSolution(
        @Header("Authorization") token: String,
        @Path("problemId") problemId: Long,
        @Body request: SubmissionDTO.SubmitSolutionRequest
    ): Response<ResponseUtilDTO.Response<SubmissionDTO.SubmitSolutionResponse>>
}