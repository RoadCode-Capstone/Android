package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.data.repository.LevelTestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RoadmapViewModel @Inject constructor(private val repository: LevelTestRepository) : ViewModel() {
    companion object {
        private const val TAG = "RoadmapViewModel"
    }

    private val _id = MutableStateFlow<Long>(0)
    val id = _id.asStateFlow()

    // 로드맵 정보 (테스트)
    val roadmapInfo = RoadmapDTO.roadmapData(
        1,
        "로드맵 제목",
        "language",
        "python",
        "",
        RoadmapDTO.roadmapProblem(
            0,
            800,
            0,
            "IN_PROGRESS"
        )
    )

    // 문제 정보 (테스트)
    val problemsInfo = listOf(
        RoadmapDTO.roadmapProblem(771, 2195, 0, "IN_PROGRESS"),
        RoadmapDTO.roadmapProblem(772, 14, 1, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(773, 20, 2, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(774, 62, 3, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(775, 98, 4, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(776, 10, 5, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(777, 209, 6, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(778, 289, 7, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(779, 331, 8, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(780, 15, 9, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(781, 45, 10, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(782, 122, 11, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(783, 11, 12, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(784, 23, 13, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(785, 7, 14, "NOT_STARTED"),
    )

    /* 로드맵 아이디 설정 함수 */
    fun setRoadmapId(id: Long) {
        _id.value = id
        Log.d(TAG, "로드맵 아이디 변경: ${_id.value}")
    }
}