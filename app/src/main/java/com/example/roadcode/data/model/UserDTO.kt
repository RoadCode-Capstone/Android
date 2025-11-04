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

    // 닉네임, 이메일 중복 체크 응답
    data class CheckDuplicatedResponse(
        val duplicated: Boolean // 중복 여부
    )

    // 비밀번호 재확인 요청
    data class VerifyPasswordRequest(
        val password: String    // 비밀번호
    )

    // 비밀번호 변경 요청
    data class EditPasswordRequest(
        val currentPassword: String,    // 현재 비밀번호
        val newPassword: String         // 새로운 비밀번호
    )

    // 회원 탈퇴 요청
    data class DeleteMemberRequest(
        val password: String    // 비밀번호
    )

    // 이메일 로그인 요청
    data class EmailLoginRequest(
        val email: String,      // 이메일
        val password: String    // 비밀번호
    )

    // 이메일 로그인 응답
    data class EmailLoginResponse(
        val accessToken: String // jwt 액세스 토큰
    )

    // 이메일 회원가입 요청
    data class EmailRegisterRequest(
        val email: String,      // 이메일
        val password: String,   // 비밀번호
        val nickname: String    // 닉네임
    )

    // 이메일로 인증코드 전송 요청
    data class VerifyEmailRequest(
        val email: String   // 이메일
    )

    // 인증코드 확인 요청
    data class VerifyCodeRequest(
        val email: String,              // 이메일
        val verificationCode: String    // 인증코드
    )

    // 비밀번호 재설정 요청
    data class ResetPasswordRequest(
        val email: String,      // 이메일
        val newPassword: String // 새로운 비밀번호
    )
}