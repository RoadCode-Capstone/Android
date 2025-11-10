package com.example.roadcode.data.model

class ReviewDTO {
    // 리뷰 작성 요청
    data class SubmitReviewRequest(
        val content: String // 리뷰 내용
    )
}