package com.example.roadcode.data.model

object LevelTestDTO {
    // 레벨 테스트 생성 요청
    data class createRequest(
        val type: String,
        val language: String,
        val algorithm: String?
    )
}