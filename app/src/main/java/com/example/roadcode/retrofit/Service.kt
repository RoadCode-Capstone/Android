package com.example.roadcode.retrofit

import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.data.model.PointDTO
import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.data.model.ResponseUtilDTO
import com.example.roadcode.data.model.ReviewDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.data.model.TagDTO
import com.example.roadcode.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

typealias ApiResponse<T> = Response<ResponseUtilDTO.Response<T>>

/* JSON 데이터 처리 */
interface JsonService {
    // 태그 목록 조회
    @GET("/api/v1/tags")
    suspend fun getTags(): ApiResponse<TagDTO.TagsResponse>

    // 레벨 테스트 생성
    @POST("/api/v1/level-test")
    suspend fun createLevelTest(
        @Header("Authorization") token: String,
        @Body request: LevelTestDTO.createRequest
    ): ApiResponse<LevelTestDTO.createResponse>

    // 레벨 테스트 문제 조회
    @GET("/api/v1/problems")
    suspend fun getLevelTestProblems(
        @Header("Authorization") token: String,
        @Query("ids") ids: List<Long>
    ):ApiResponse<LevelTestDTO.getResponse>

    // 레벨 테스트 제출
    @POST("/api/v1/level-test/submissions")
    suspend fun submitLevelTest(
        @Header("Authorization") token: String,
        @Body request: LevelTestDTO.submitRequest
    ): ApiResponse<LevelTestDTO.submitResponse>

    // 로드맵 생성
    @POST("/api/v1/roadmaps")
    suspend fun createRoadmap(
        @Header("Authorization") token: String,
        @Body request: RoadmapDTO.CreateRequest
    ): ApiResponse<RoadmapDTO.CreateResponse>

    // 로드맵 정보 조회
    @GET("/api/v1/roadmaps/{roadmapId}")
    suspend fun getRoadmap(
        @Header("Authorization") token: String,
        @Path("roadmapId") roadmapId: Long
    ): ApiResponse<RoadmapDTO.RoadmapData>

    // 로드맵 문제 목록 조회
    @GET("api/v1/roadmaps/{roadmapId}/problems")
    suspend fun getRoadmapProblems(
        @Header("Authorization") token: String,
        @Path("roadmapId") roadmapId: Long
    ): ApiResponse<RoadmapDTO.GetProblemsResponse>

    // 문제 정보 조회
    @GET("/api/v1/problems/{problemId}")
    suspend fun getProblem(
        @Header("Authorization") token: String,
        @Path("problemId") problemId: Long
    ): ApiResponse<ProblemDTO.ProblemData>

    // 로드맵 포기
    @POST("/api/v1/roadmaps/{roadmapId}/give-up")
    suspend fun giveUpRoadmap(
        @Header("Authorization") token: String,
        @Path("roadmapId") roadmapId: Long
    ): ApiResponse<Nothing>

    // 회원 로드맵 목록 조회
    @GET("/api/v1/roadmaps/my")
    suspend fun getRoadmaps(
        @Header("Authorization") token: String,
        @Query("statusList") statusList: List<String>?
    ): ApiResponse<RoadmapDTO.GetRoadmapsResponse>

    // 순위 조회
    @GET("api/v1/points/ranking")
    suspend fun getRanking(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): ApiResponse<PointDTO.GetRankingResponse>

    // 날짜별 포인트 내역 조회
    @GET("/api/v1/points/my?groupBy=DATE")
    suspend fun getPointsByDate(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ):ApiResponse<PointDTO.GetPointsByDateResponse>

    // 종류별 포인트 내역 조회
    @GET("/api/v1/points/my?groupBy=TYPE")
    suspend fun getPointsByType(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String
    ):ApiResponse<PointDTO.GetPointsByTypeResponse>

    // 회원 정보 조회
    @GET("/api/v1/member")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): ApiResponse<UserDTO.GetUserInfoResponse>

    // 회원 정보 수정
    @PUT("/api/v1/member")
    suspend fun editUserInfo(
        @Header("Authorization") token: String,
        @Body request: UserDTO.EditUserInfoRequest
    ): ApiResponse<Nothing>

    // 닉네임 중복 체크
    @GET("/api/v1/member/exists-nickname")
    suspend fun checkDuplicatedNickname(
        @Query("nickname") nickname: String
    ): ApiResponse<UserDTO.CheckDuplicatedResponse>

    // 비밀번호 재확인
    @POST("/api/v1/member/verify-password")
    suspend fun verifyPassword(
        @Header("Authorization") token: String,
        @Body request: UserDTO.VerifyPasswordRequest
    ): ApiResponse<Nothing>

    // 비밀번호 변경
    @PUT("/api/v1/member/password")
    suspend fun editPassword(
        @Header("Authorization") token: String,
        @Body request: UserDTO.EditPasswordRequest
    ): ApiResponse<Nothing>

    // 출석 체크
    @POST("/api/v1/points/attendance/check")
    suspend fun checkAttendance(
        @Header("Authorization") token: String
    ): ApiResponse<Nothing>

    // 풀이 제출
    @POST("/api/v1/problems/{problemId}/submission")
    suspend fun submitSolution(
        @Header("Authorization") token: String,
        @Path("problemId") problemId: Long,
        @Body request: SubmissionDTO.SubmitSolutionRequest
    ): ApiResponse<SubmissionDTO.SubmitSolutionResponse>

    // 회원 탈퇴
    @HTTP(method = "DELETE", path = "/api/v1/member", hasBody = true)
    suspend fun deleteMember(
        @Header("Authorization") token: String,
        @Body request: UserDTO.DeleteMemberRequest
    ): ApiResponse<Nothing>

    // 이메일 로그인
    @POST("/api/v1/auth/login")
    suspend fun emailLogin(
        @Body request: UserDTO.EmailLoginRequest
    ): ApiResponse<UserDTO.EmailLoginResponse>

    // 이메일 회원가입
    @POST("/api/v1/auth/signup")
    suspend fun emailRegister(
        @Body request: UserDTO.EmailRegisterRequest
    ): ApiResponse<Nothing>

    // 이메일로 인증코드 전송 - 회원가입
    @POST("/api/v1/auth/signup/verify-email")
    suspend fun verifyEmailRegister(
        @Body request: UserDTO.VerifyEmailRequest
    ): ApiResponse<Nothing>

    // 이메일로 인증코드 전송 - 비밀번호 재설정
    @POST("/api/v1/auth/reset-password/verify-email")
    suspend fun verifyEmailPassword(
        @Body request: UserDTO.VerifyEmailRequest
    ): ApiResponse<Nothing>

    // 인증코드 확인
    @POST("/api/v1/auth/verify-code")
    suspend fun verifyCode(
        @Body request: UserDTO.VerifyCodeRequest
    ): ApiResponse<Nothing>

    // 이메일 중복 체크
    @GET("/api/v1/member/exists-email")
    suspend fun checkDuplicatedEmail(
        @Query("email") email: String
    ): ApiResponse<UserDTO.CheckDuplicatedResponse>

    // 로그아웃
    @POST("/api/v1/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): ApiResponse<Nothing>

    // 비밀번호 재설정
    @POST("/api/v1/auth/reset-password")
    suspend fun resetPassword(
        @Body request: UserDTO.ResetPasswordRequest
    ): ApiResponse<Nothing>

    // 리뷰 작성
    @POST("/api/v1/submissions/{submissionId}/reviews")
    suspend fun submitReview(
        @Header("Authorization") token: String,
        @Path("submissionId") submissionId: Long,
        @Body request: ReviewDTO.SubmitReviewRequest
    ): ApiResponse<Nothing>

    // 다른 사람 풀이 목록 조회
    @GET("/api/v1/problem/{problemId}/submissions/success")
    suspend fun getOtherSubmissions(
        @Header("Authorization") token: String,
        @Path("problemId") problemId: Long
    ): ApiResponse<SubmissionDTO.GetOtherSubmissionsResponse>

    // 본인 풀이 목록 조회
    @GET("/api/v1/submissions")
    suspend fun getMySubmissions(
        @Header("Authorization") token: String,
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("isSuccess") isSuccess: Boolean?
    ): ApiResponse<SubmissionDTO.GetMySubmissionsResponse>
}