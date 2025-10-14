package com.example.roadcode.data.model

object SubmissionDTO {
    // 풀이 데이터 구조
    data class SubmissionData(
        val problemId: Long,    // 문제 ID
        val language: String,   // 언어
        val sourceCode: String  // 풀이 코드
    )

    // 풀이 제출 요청
    data class SubmitSolutionRequest(
        val language: String,   // 언어
        val sourceCode: String  // 소스 코드
    )

    // 풀이 제출 응답
    data class SubmitSolutionResponse(
        val allPassed: Boolean,                         // 테스트케이스 전체 통과 여부
        val testcaseResults: List<TestcaseResultData>   // 테스트케이스 결과 목록
    )

    // 테스트케이스 결과
    data class TestcaseResultData(
        val passed: Boolean,    // 통과 여부
        val message: String     // 통과 못한 경우, 오류 생긴 경우 메시지
    )
}