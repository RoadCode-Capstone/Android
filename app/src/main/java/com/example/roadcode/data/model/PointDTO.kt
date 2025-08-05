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
}