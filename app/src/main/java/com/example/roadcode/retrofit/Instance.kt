package com.example.roadcode.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://3.35.192.94:8080" // BASE URL 설정

    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    // timeout 설정 추가
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // 연결 시도 제한 시간
        .readTimeout(30, TimeUnit.SECONDS)    // 서버 응답 대기 시간
        .writeTimeout(30, TimeUnit.SECONDS)   // 서버로 데이터 쓰는 시간
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val jsonService: JsonService = retrofit.create(JsonService::class.java)
}