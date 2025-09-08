package com.example.roadcode.data.model

object UserDTO {
    // 회원 정보 조회 응답
    data class GetUserInfoResponse(
        val email: String,      // 이메일
        val nickname: String    // 닉네임
    )

    // 회원 정보 수정 요청
    data class EditUserInfoRequest(
        val nickname: String    // 닉네임
    )

    // 닉네임 중복 체크 응답
    data class CheckNicknameResponse(
        val duplicated: Boolean // 닉네임 중복 여부
    )
}