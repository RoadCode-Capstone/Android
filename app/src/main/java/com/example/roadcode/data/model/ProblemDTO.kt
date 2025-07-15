package com.example.roadcode.data.model

class ProblemDTO {
    // 문제 데이터 구조
    data class ProblemData(
        val problemId: Long,            // 문제 ID
        val contestId: Int,             // 대회 ID
        val index: String,              // 문제 인덱스
        val name: String,               // 문제 이름
        val rating: Int,                // 문제 난이도
        val description: String,        // 문제 설명
        val inputDescription: String,   // 입력값 설명
        val outputDescription: String,  // 출력값 설명
        val timeLimit: String,          // 시간 제한
        val memoryLimit: String,        // 메모리 제한
        val url: String,                // 문제 원문 url
        val tags: List<String>          // 문제에 포함된 알고리즘(태그)
    )
}