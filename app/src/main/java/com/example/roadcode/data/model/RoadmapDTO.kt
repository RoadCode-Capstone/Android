package com.example.roadcode.data.model

object RoadmapDTO {
    // 로드맵 데이터 구조
    data class RoadmapData(
        val roadmapId: Long = 0,                                // 로드맵 ID
        val title: String = "",                                 // 제목
        val type: String = "",                                  // 유형
        val language: String = "",                              // 언어
        val algorithm: String = "",                             // 알고리즘
        val currentProblem: RoadmapProblem = RoadmapProblem(),  // 해당 로드맵에서 현재 진행 중인 문제 정보
        val levelTestResult: Int = 0,                           // 레벨 테스트 결과
        val dailyGoal: Int = 0                                  // 일일 학습 목표
    )

    // 해당 로드맵에서 현재 진행 중인 문제 데이터 구조
    data class RoadmapProblem(
        val roadmapProblemId: Long = 0, // 로드맵 내 문제 ID
        val problemId: Long = 0,        // 문제 ID
        val order: Int = 0,             // 로드맵 내 문제 순서 (0부터 시작)
        val status: String = ""         // 문제 풀이 상태 (시작 전: NOT_STARTED, 진행 중: IN_PROGRESS, 완료: COMPLETED)
    )

    // 로드맵 생성 요청
    data class CreateRequest(
        val type: String,           // 유형
        val language: String,       // 언어
        val algorithm: String?,     // 알고리즘
        val dailyGoal: Int,         // 일일 학습 목표
        val levelTestResult: Int    // 레벨 테스트 측정 결과
    )

    // 로드맵 생성 응답
    data class CreateResponse(
        val id: Long    // 생성된 로드맵 ID
    )

    // 로드맵 문제 목록 조회 응답
    data class GetProblemsResponse(
        val roadmapProblems: List<RoadmapProblem>
    )

    // 회원 로드맵 목록 조회 응답
    data class GetRoadmapsResponse(
        val roadmaps: List<RoadmapsData> // 로드맵 목록 리스트
    )

    // 로드맵 목록 데이터 구조
    data class RoadmapsData(
        val roadmapId: Long,    // 로드맵 ID
        val title: String,      // 제목
        val type: String,       // 유형
        val language: String,   // 언어
        val algorithm: String,  // 알고리즘
        val status: String      // 로드맵 상태 (진행 중: IN_PROGRESS, 완료: COMPLETED, 포기: GAVE_UP)
    )
}