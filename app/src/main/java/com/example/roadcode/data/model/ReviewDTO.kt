package com.example.roadcode.data.model

class ReviewDTO {
    // 리뷰 답글 작성 요청
    data class SubmitReviewCommentRequest(
        val content: String // 리뷰 답글 내용
    )

    // 리뷰 답글 조회 응답
    data class GetReviewCommentResponse(
        val reviews: List<ReviewData>   // 리뷰 리스트
    )

    // 리뷰 데이터 구조
    data class ReviewData(
        val reviewId: Long = 0,                         // 리뷰 ID
        val memberId: Long = 0,                         // 작성자 ID
        val nickname: String = "",                      // 작성자 닉네임
        val content: String = "",                       // 리뷰 내용
        val comments: List<CommentData> = emptyList(),  // 답글 리스트
        val createdAt: String = ""                      // 작성 일자
    )

    // 답글 데이터 구조
    data class CommentData(
        val commentId: Long,    // 답글 ID
        val memberId: Long,     // 작성자 ID
        val nickname: String,   // 작성자 닉네임
        val content: String,    // 답글 내용
        val createdAt: String   // 작성 일자
    )
}