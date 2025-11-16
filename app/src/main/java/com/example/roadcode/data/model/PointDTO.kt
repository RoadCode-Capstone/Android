package com.example.roadcode.data.model

object PointDTO {
    // 순위 데이터 구조
    data class RankData(
        val memberId: Long,     // 사용자 아이디
        val nickname: String,   // 사용자 닉네임
        val totalPoint: Int,    // 기간 내 포인트 총합
        val rank: Int           // 순위 (포인트가 동일한 경우 동일한 순위)
    )

    // 순위 조회 응답
    data class GetRankingResponse(
        val myRank: Int,            // 사용자 순위
        val ranks: List<RankData>   // 전체 순위 목록
    )

    // 날짜별 포인트 내역 데이터 구조
    data class PointsByDateData(
        val date: String,                       // 날짜
        val totalPoint: Int,                    // 해당 날짜에 획득한 총 포인트
        val pointDetails: List<PointDetailData> // 포인트 내역
    )

    // 포인트 내역 데이터 구조
    data class PointDetailData(
        val type: String,   // 포인트 종류
        val point: Int      // 포인트 획득량
    )

    // 날짜별 포인트 내역 조회 응답
    data class GetPointsByDateResponse(
        val totalPoint: Int,                // 기간 내 총 포인트
        val history: List<PointsByDateData> // 날짜별 포인트 내역
    )

    // 종류별 포인트 내역 조회 응답
    data class GetPointsByTypeResponse(
        val totalPoint: Int,                // 기간 내 총 포인트
        val history: List<PointsByTypeData> // 종류별 포인트 내역
    )

    // 종류별 포인트 내역 데이터 구조
    data class PointsByTypeData(
        val type: String,       // 포인트 종류
        val totalPoint: Int,    // 해당 종류에 대해 획득한 총 포인트
        val point: Int,         // 해당 종류로 얻을 수 있는 포인트 (1개 달성 기준)
        val dates: List<String> // 획득한 날짜 목록
    )
}