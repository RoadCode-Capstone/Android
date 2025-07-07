package com.example.roadcode.data.model

/* 공통 DTO */
object ResponseUtilDTO {
    data class Response(
        val code: String,
        val message: String? = null,
        val data: Map<String, Any>? = null
    )
}