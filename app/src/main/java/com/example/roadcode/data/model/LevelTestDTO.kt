package com.example.roadcode.data.model

object LevelTestDTO {
    // 레벨 테스트 생성 요청
    data class createRequest(
        val type: String,       // 유형
        val language: String,   // 언어
        val algorithm: String?  // 알고리즘
    )

    // 레벨 테스트 생성 응답
    data class createResponse(
        val problemIds: List<Long>  // 레벨 테스트 문제 ID 리스트
    )

    // 레벨 테스트 문제 조회 응답
    data class getResponse(
        val problems: List<ProblemDTO.ProblemData>  // 문제 데이터 리스트
    )

    // 레벨 테스트 제출 요청
    data class submitRequest(
        val submissions: List<SubmissionDTO.SubmissionData> // 풀이 데이터 리스트
    )

    // 레벨 테스트 제출 응답
    data class submitResponse(
        val totalProblems: Int,     // 총 문제 수
        val result: List<Boolean>,  // 결과 리스트
        val passedCount: Int        // 통과한 문제 수
    )
}