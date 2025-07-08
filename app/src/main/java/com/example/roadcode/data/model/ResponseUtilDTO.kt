package com.example.roadcode.data.model

/* 공통 DTO */
object ResponseUtilDTO {
    data class Response<T>(
        val code: String,
        val message: String?,
        val data: T?
    )
}