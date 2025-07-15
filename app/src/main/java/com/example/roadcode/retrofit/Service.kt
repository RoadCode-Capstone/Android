package com.example.roadcode.retrofit

import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.data.model.ResponseUtilDTO
import com.example.roadcode.data.model.TagDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/* JSON 데이터 처리 */
interface JsonService {
    // 태그 목록 조회
    @GET("/api/v1/tags")
    suspend fun getTags(): Response<ResponseUtilDTO.Response<TagDTO.TagsResponse>>

    // 레벨 테스트 생성
    @POST("/api/v1/level-test")
    suspend fun createLevelTest(@Header("Authorization") token: String, @Body request: LevelTestDTO.createRequest): Response<ResponseUtilDTO.Response<LevelTestDTO.createResponse>>

    // 레벨 테스트 문제 조회
    @GET("/api/v1/problems")
    suspend fun getLevelTestProblems(@Header("Authorization") token: String, @Query("ids") ids: List<Long>): Response<ResponseUtilDTO.Response<LevelTestDTO.getResponse>>

    // 레벨 테스트 제출
    @POST("/api/v1/level-test/submissions")
    suspend fun submitLevelTest(@Header("Authorization") token: String, @Body request: LevelTestDTO.submitRequest): Response<ResponseUtilDTO.Response<LevelTestDTO.submitResponse>>
}