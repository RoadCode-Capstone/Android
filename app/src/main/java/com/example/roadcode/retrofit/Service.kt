package com.example.roadcode.retrofit

import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.data.model.ResponseUtilDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/* JSON 데이터 처리 */
interface JsonService {
    // 태그 목록 조회
    @GET("/api/v1/tags")
    suspend fun getTags(@Header("Authorization") token: String): Response<ResponseUtilDTO.Response>

    // 레벨 테스트 생성
    @POST("/api/v1/level-test")
    suspend fun createLevelTest(@Header("Authorization") token: String, @Body request: LevelTestDTO.createRequest): Response<ResponseUtilDTO.Response>
}