package com.example.roadcode.data.repository

import com.example.roadcode.data.model.ReviewDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.retrofit.JsonService
import com.example.roadcode.retrofit.RetrofitInstance
import javax.inject.Inject

class ReviewRepository @Inject constructor(private val tokenRepository: TokenRepository) {
    private val jsonService: JsonService = RetrofitInstance.retrofit.create(JsonService::class.java)

    /* 로드맵 생성 */
    suspend fun createRoadmap(request: RoadmapDTO.CreateRequest) =
        ResponseHandler.handleResponse { jsonService.createRoadmap(tokenRepository.getBearerToken(), request) }

    /* 리뷰 작성 */
    suspend fun submitReview(submissionId: Long, request: ReviewDTO.SubmitReviewCommentRequest) =
        ResponseHandler.handleResponse { jsonService.submitReview(tokenRepository.getBearerToken(), submissionId, request) }

    /* 답글 작성 */
    suspend fun submitComment(reviewId: Long, request: ReviewDTO.SubmitReviewCommentRequest) =
        ResponseHandler.handleResponse { jsonService.submitComment(tokenRepository.getBearerToken(), reviewId, request) }

    /* 리뷰 답글 조회 */
    suspend fun getReviewComment(submissionId: Long) =
        ResponseHandler.handleResponse { jsonService.getReviewComment(tokenRepository.getBearerToken(), submissionId) }

}