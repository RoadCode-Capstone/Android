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

data class ReviewUiState(
    val complete: Int = 0,                                                                          // 작성 완료한 리뷰 수
    val problemId: Long = 0,                                                                        // 문제 아이디
    val submissionInfo: SubmissionDTO.OtherSubmissionsData = SubmissionDTO.OtherSubmissionsData(),  // 리뷰 작성할 풀이 정보
    val submissions: List<SubmissionDTO.OtherSubmissionsData> = emptyList(),                        // 다른 사람 풀이 목록
    val review: String = ""                                                                         // 작성한 리뷰
)

@HiltViewModel
class ReviewViewModel @Inject constructor(private val repository: ReviewRepository, private val submissionRepository: SubmissionRepository) : ViewModel() {
    companion object {
        private const val TAG = "ReviewViewModel"
    }

    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast = _toast.asSharedFlow()
    private val _navigateBack = MutableSharedFlow<Unit>()
    val navigateBack = _navigateBack.asSharedFlow()

    private val _reviewUiState = MutableStateFlow(ReviewUiState())
    val reviewUiState = _reviewUiState.asStateFlow()

    /* 문제 아이디 변경 함수 */
    fun setProblemId(problemId: Long) {
        _reviewUiState.update { it.copy(problemId = problemId) }
    }

    /* 리뷰 작성할 풀이 정보 변경 함수 */
    fun setSubmissionInfo(submissionInfo: SubmissionDTO.OtherSubmissionsData) {
        _reviewUiState.update { it.copy(submissionInfo = submissionInfo) }
    }

    /* 리뷰 입력 처리 함수 */
    fun updateReview(value: String) {
        _reviewUiState.update { it.copy(review = value) }
    }

    /* 다른 사람 풀이 목록 조회 함수 */
    fun getOtherSolutions() {
        viewModelScope.launch {
            submissionRepository.getOtherSubmissions(reviewUiState.value.problemId).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _reviewUiState.update { it.copy(submissions = body.data!!.submissions) }

                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "다른 사람 풀이 목록 조회 성공: ${body.message}")
                            }
                            "E001" -> { // 사용자 토큰이 잘못된 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "다른 사람 풀이 목록 조회 실패: 사용자 토큰이 잘못된 경우")
                            }
                            "E014" -> { // 경로의 문제 id가 잘못된 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "다른 사람 풀이 목록 조회 실패: 경로의 문제 id가 잘못된 경우")
                            }
                            "E022" -> { // 사용자가 해당 문제를 풀지 않은 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "다른 사람 풀이 목록 조회 실패: 사용자가 해당 문제를 풀지 않은 경우")
                            }
                            else -> Log.d(TAG, "다른 사람 풀이 목록 조회 실패: 알 수 없는 오류")
                        }

                    }
                    .onFailure { e ->
                        e.printStackTrace()
                    }
            }
        }
    }

    /* 리뷰 등록 함수 */
    fun submitReview() {
        val request = ReviewDTO.SubmitReviewRequest(content = reviewUiState.value.review)

        viewModelScope.launch {
            repository.submitReview(reviewUiState.value.submissionInfo.submissionId, request).collect { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _reviewUiState.update { it.copy(complete = it.complete + 1) }

                                _navigateBack.emit(Unit)
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "리뷰 작성 성공: ${body.message}")
                            }
                            "E001" -> { // 사용자 email이 잘못된 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "리뷰 작성 실패: 사용자 email이 잘못된 경우")
                            }
                            "E022" -> { // 사용자가 해당 문제를 풀지 않았을 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "리뷰 작성 실패: 사용자가 해당 문제를 풀지 않았을 경우")
                            }
                            "E023" -> { // 문제 풀이 id가 잘못된 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "리뷰 작성 실패: 문제 풀이 id가 잘못된 경우")
                            }
                            "E024" -> { // 자기 자신의 풀이에 리뷰를 다는 경우
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "리뷰 작성 실패: 자기 자신의 풀이에 리뷰를 다는 경우")
                            }
                            else -> Log.d(TAG, "리뷰 작성 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }
}