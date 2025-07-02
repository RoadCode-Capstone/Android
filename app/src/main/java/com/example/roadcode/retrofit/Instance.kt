package com.example.roadcode.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://3.35.192.94:8080" // BASE URL 설정

    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()) //텍스트 데이터 처리
            .addConverterFactory(GsonConverterFactory.create(gson)) //Json 데이터 처리
            .build()
    }

    val jsonService: JsonService = retrofit.create(JsonService::class.java)
}