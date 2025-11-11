package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.ReviewDTO
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.data.repository.ReviewRepository
import com.example.roadcode.data.repository.SubmissionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ViewReviewUiState(
    val problemId: Long = 0,    // 문제 아이디
    val submissionId: Long = 0, // 풀이 아이디
    val problemInfo: List<String> = emptyList(), // 문제 정보
    val submissionInfo: SubmissionDTO.GetSubmissionResponse = SubmissionDTO.GetSubmissionResponse(), // 풀이 정보
    val aiReview: ReviewDTO.ReviewData = ReviewDTO.ReviewData(),    // AI 리뷰 정보
    val reviewComments: List<ReviewDTO.ReviewData> = emptyList(),   // 사용자 리뷰 답글 정보
    val comments: Map<Long, String> = emptyMap(),   // 작성한 답글 내용 (리뷰 아이디, 답글 내용)
)

@HiltViewModel
class ViewReviewViewModel @Inject constructor(private val repository: SubmissionRepository, private val reviewRepository: ReviewRepository) : ViewModel() {
    companion object {
        private const val TAG = "ViewReviewViewModel"
    }

    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast = _toast.asSharedFlow()

    private val _viewReviewUiState = MutableStateFlow(ViewReviewUiState())
    val viewReviewUiState = _viewReviewUiState.asStateFlow()

    /* 문제 아이디, 풀이 아이디 설정 함수 */
    fun setIds(problemId: Long, submissionId: Long) {
        _viewReviewUiState.update { it.copy(problemId = problemId, submissionId = submissionId) }
    }

    /* 답글 입력 초기화 함수 */
    fun initComment() {
        _viewReviewUiState.update {
            it.copy(comments = emptyMap())
        }
    }

    /* 답글 입력 처리 함수 */
    fun updateComment(reviewId: Long, value: String) {
        _viewReviewUiState.update {
            val updatedComments = it.comments.toMutableMap()
            updatedComments[reviewId] = value
            it.copy(comments = updatedComments)
        }
    }

    /* 문제 정보 조회 함수 */
    fun getProblem() {
        viewModelScope.launch {
            repository.getProblem(viewReviewUiState.value.problemId).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val problemInfo = body.data!!
                                val info = listOf(
                                    problemInfo.name,
                                    problemInfo.description,
                                    problemInfo.inputDescription,
                                    problemInfo.outputDescription,
                                    problemInfo.timeLimit,
                                    problemInfo.memoryLimit
                                )

                                _viewReviewUiState.update { it.copy(problemInfo = info) }

                                Log.d(TAG, "문제 정보 조회 성공\n${viewReviewUiState.value.problemInfo}")
                            }
                            "E001" -> { // 사용자를 찾을 수 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "문제 정보 조회 실패: 사용자를 찾을 수 없음")
                            }
                            "E002" -> { // 토큰 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "문제 정보 조회 실패: 토큰 없음")
                            }
                            "E014" -> { // 문제 id가 잘못된 경우(문제가 없는 경우)
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "문제 정보 조회 실패: 문제 id가 잘못된 경우(문제가 없는 경우)")
                            }
                            else -> Log.d(TAG, "문제 정보 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 풀이 정보 조회 함수 */
    fun getSubmission() {
        viewModelScope.launch {
            repository.getSubmission(viewReviewUiState.value.submissionId).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _viewReviewUiState.update { it.copy(submissionInfo = body.data!!) }

                                Log.d(TAG, "풀이 정보 조회 성공\n${viewReviewUiState.value.submissionInfo}")
                            }
                            "E023" -> { // 풀이를 찾을 수 없는 경우 (풀이 id가 잘못됨)
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "풀이 정보 조회 실패: 풀이를 찾을 수 없는 경우 (풀이 id가 잘못됨)")
                            }
                            else -> Log.d(TAG, "풀이 정보 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 리뷰 답글 조회 함수 */
    fun getReviewComment() {
        viewModelScope.launch {
            reviewRepository.getReviewComment(viewReviewUiState.value.submissionId).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _viewReviewUiState.update { it.copy(reviewComments = body.data!!.reviews) }

                                Log.d(TAG, "리뷰 답글 조회 성공\n${viewReviewUiState.value.reviewComments}")
                            }
                            "E001" -> { // 사용자 토큰이 잘못되었을 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "리뷰 답글 조회 실패: 사용자 토큰이 잘못되었을 경우")
                            }
                            "E022" -> { // 사용자가 해당 풀이에 대한 문제를 풀지 않고 접근할 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "리뷰 답글 조회 실패: 사용자가 해당 풀이에 대한 문제를 풀지 않고 접근할 경우")
                            }
                            "E023" -> { // 경로의 풀이 id가 잘못되었을 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "리뷰 답글 조회 실패: 경로의 풀이 id가 잘못되었을 경우")
                            }
                            else -> Log.d(TAG, "리뷰 답글 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 답글 작성 함수 */
    fun submitComment(reviewId: Long) {
        val request = ReviewDTO.SubmitReviewCommentRequest(viewReviewUiState.value.comments[reviewId] ?: "")
Log.d(TAG, "답글 작성 요청\n${request}")
        viewModelScope.launch {
            reviewRepository.submitComment(reviewId, request).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                initComment()
                                getReviewComment()

                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "답글 작성 성공\n${viewReviewUiState.value.reviewComments}")
                            }
                            "E001" -> { // 사용자 email이 잘못된 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "답글 작성 실패: 사용자 email이 잘못된 경우")
                            }
                            "E022" -> { // 사용자가 해당 리뷰가 달린 문제를 풀지 않았을 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "답글 작성 실패: 사용자가 해당 리뷰가 달린 문제를 풀지 않았을 경우")
                            }
                            "E025" -> { // 리뷰 id가 잘못된 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "답글 작성 실패: 리뷰 id가 잘못된 경우")
                            }
                            "E031" -> { // AI 유효성 검사를 통과하지 못한 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "답글 작성 실패: AI 유효성 검사를 통과하지 못한 경우")
                            }
                            else -> Log.d(TAG, "답글 작성 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }
}