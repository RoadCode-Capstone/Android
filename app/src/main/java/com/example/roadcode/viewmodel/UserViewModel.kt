package com.example.roadcode.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roadcode.data.model.UserDTO
import com.example.roadcode.data.repository.TokenRepository
import com.example.roadcode.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserInfoUiState(
    val isEdit: Boolean = false,
    val userInfo: UserDTO.GetUserInfoResponse = UserDTO.GetUserInfoResponse("", ""),
    val nicknameInput: String = userInfo.nickname,
    val isAvailable: Boolean = false,
    val supportingText: String = "현재 닉네임과 같습니다."
)

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository, private val tokenRepository: TokenRepository) : ViewModel() {
    companion object {
        private const val TAG = "UserViewModel"
    }

    private val _userInfoUiState = MutableStateFlow(UserInfoUiState())
    val userInfoUiState = _userInfoUiState.asStateFlow()
    private val _toast = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val toast = _toast.asSharedFlow()

    private var nicknameCheckJob: Job? = null   // 마지막 값 입력 후 0.5초 뒤에 닉네임 중복 체크 위한 Job

    init {
        viewModelScope.launch {
            tokenRepository.tokenFlow.collect { token ->
                if (!token.isNullOrBlank()) {
                    getUserInfo()
                    cancel()
                }
            }
        }
    }

    /* 정보 수정 여부 변경 함수 */
    fun setEditMode(isEdit: Boolean) {
        _userInfoUiState.update {
            if (isEdit) {
                it.copy(isEdit = true, nicknameInput = it.userInfo.nickname, isAvailable = false, supportingText = "현재 닉네임과 같습니다.")
            }
            else {
                it.copy(isEdit = false)
            }
        }
    }

    /* 입력한 닉네임 변경 함수 */
    fun updateNicknameInput(value: String) {
        _userInfoUiState.update { it.copy(nicknameInput = value) }

        nicknameCheckJob?.cancel()  // 이전 작업 취소

        // 기존 닉네임과 같은 경우
        if (_userInfoUiState.value.userInfo.nickname == value) {
            _userInfoUiState.update { it.copy(isAvailable = false, supportingText = "현재 닉네임과 같습니다.") }
            return
        }

        nicknameCheckJob = viewModelScope.launch {
            delay(500L) // 0.5초 후 닉네임 중복 체크
            checkDuplicatedNickname(value)
        }
    }

    /* 회원 정보 조회 함수 */
    fun getUserInfo() {
        viewModelScope.launch {
            repository.getUserInfo().collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val userInfo = body.data!!
                                _userInfoUiState.update { it.copy(userInfo = userInfo) }

                                Log.d(TAG, "회원 정보 조회 성공\n${userInfo}")
                            }
                            "E001" -> { // 사용자를 찾을 수 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 정보 조회 실패: 사용자를 찾을 수 없음")
                            }
                            "E002" -> { // 토큰 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 정보 조회 실패: 토큰 없음")
                            }
                            else -> Log.d(TAG, "회원 정보 조회 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }

    /* 회원 정보 수정 함수 */
    fun editUserInfo() {
        viewModelScope.launch {
            val request = UserDTO.EditUserInfoRequest(userInfoUiState.value.nicknameInput)

            repository.editUserInfo(request).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                _userInfoUiState.update {
                                    it.copy(
                                        isEdit = false,
                                        userInfo = it.userInfo.copy(nickname = it.nicknameInput)
                                    )
                                }

                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 정보 수정 성공: ${body.message}")
                            }
                            "E001" -> { // 사용자를 찾을 수 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 정보 수정 실패: 사용자를 찾을 수 없음")
                            }
                            "E002" -> { // 토큰 없음
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 정보 수정 실패: 토큰 없음")
                            }
                            "E005" -> { // 닉네임 중복
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 정보 수정 실패: 닉네임 중복")
                            }
                            "ERROR" -> { // 닉네임 입력 X
                                _toast.emit(body.message ?: "")
                                Log.d(TAG, "회원 정보 수정 실패: ${body.message}")
                            }
                            else -> Log.d(TAG, "회원 정보 수정 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }

    /* 닉네임 중복 체크 함수 */
    fun checkDuplicatedNickname(nickname: String) {
        viewModelScope.launch {
            repository.checkDuplicatedNickname(nickname).collect() { result ->
                result
                    .onSuccess { body ->
                        when (body.code) {
                            "SUCCESS" -> {
                                val duplicated = body.data!!.duplicated
                                _userInfoUiState.update {
                                    it.copy(
                                        isAvailable = !duplicated,
                                        supportingText = if (duplicated) "중복되는 닉네임입니다." else "사용 가능한 닉네임입니다."
                                    )
                                }

                                Log.d(TAG, "닉네임 중복 체크 성공: ${duplicated}")
                            }
                            else -> Log.d(TAG, "닉네임 중복 체크 실패: 알 수 없는 오류")
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "네트워크 오류: ${e.message}")
                    }
            }
        }
    }
}