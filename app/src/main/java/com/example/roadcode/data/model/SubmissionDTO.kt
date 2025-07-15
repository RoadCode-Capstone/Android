package com.example.roadcode.data.model

object SubmissionDTO {
    // 풀이 데이터 구조
    data class SubmissionData(
        val problemId: Long,    // 문제 ID
        val language: String,   // 언어
        val sourceCode: String  // 풀이 코드
    )
}