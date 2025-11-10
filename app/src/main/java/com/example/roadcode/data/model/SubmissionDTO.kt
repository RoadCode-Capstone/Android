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
        val roadmapId: Long,        // 로드맵 ID
        val roadmapProblemId: Long, // 로드맵 문제 ID
        val language: String,       // 언어
        val sourceCode: String      // 소스 코드
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

    // 다른 사람 풀이 목록 조회 응답
    data class GetOtherSubmissionsResponse(
        val submissions: List<OtherSubmissionsData> // 다른 사람 풀이 목록
    )

    // 다른 사람 풀이 목록 데이터 구조
    data class OtherSubmissionsData(
        val submissionId: Long = 0,     // 풀이 ID
        val language: String = "",      // 소스코드 작성 언어
        val sourceCode: String = "",    // 풀이 코드
        val nickname: String = "",      // 풀이를 작성한 사용자 닉네임
        val createdAt: String = ""      // 생성일자
    )

    // 본인 풀이 목록 조회 응답
    data class GetMySubmissionsResponse(
        val history: List<MySubmissionsData>
    )

    // 본인 풀이 목록 데이터 구조
    data class MySubmissionsData(
        val date: String,
        val submissionDetails: List<MySubmissionData>
    )

    // 본인 풀이 데이터 구조
    data class MySubmissionData(
        val problemId: Long,
        val problemName: String,
        val submissionId: Long,
        val isSuccess: Boolean
    )
}