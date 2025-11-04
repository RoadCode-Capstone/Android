package com.example.roadcode.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.util.rememberOnce
import com.example.roadcode.view.component.LoadingOverlay
import com.example.roadcode.view.component.LoadingOverlayName
import com.example.roadcode.viewmodel.RegisterField
import com.example.roadcode.viewmodel.RegisterUiState
import com.example.roadcode.viewmodel.RegisterViewModel
import com.example.roadcode.viewmodel.ResetPasswordField
import com.example.roadcode.viewmodel.ResetPasswordUiState
import com.example.roadcode.viewmodel.ResetPasswordViewModel

/* 비밀번호 재설정 화면 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(navController: NavController, resetPasswordViewModel: ResetPasswordViewModel) {
    val context = LocalContext.current

    val navigateBack by resetPasswordViewModel.navigateBack.collectAsState()
    val isLoading by resetPasswordViewModel.isLoading.collectAsState()

    val resetPasswordUiState by resetPasswordViewModel.resetPasswordUiState.collectAsState()

    LaunchedEffect(Unit) {
        resetPasswordViewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            navController.popBackStack()
            resetPasswordViewModel.setFalseNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "비밀번호 초기화",
                        fontSize = 18.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = rememberOnce { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기 버튼",
                            tint = PrimaryColor
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = PrimaryColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                ResetPasswordCard(resetPasswordUiState, resetPasswordViewModel)
            }

            LoadingOverlay(isLoading, LoadingOverlayName.DEFAULT)
        }
    }
}

/* 비밀번호 초기화 카드 */
@Composable
fun ResetPasswordCard(resetPasswordUiState: ResetPasswordUiState, resetPasswordViewModel: ResetPasswordViewModel) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 30.dp, horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 이메일 입력 & 이메일 인증코드 발송 버튼
            EmailBar(
                resetPasswordUiState.email,
                resetPasswordUiState.verifiedEmail,
                changedInput = { resetPasswordViewModel.updateInput(ResetPasswordField.EMAIL, it) },
                clickedBtn = { resetPasswordViewModel.verifyEmailRegister() }
            )

            // 인증코드 입력 & 인증코드 발송 버튼(재발송) & 5분 타이머 & 인증코드 확인 버튼
            VerifyCodeBar(
                resetPasswordUiState.code,
                resetPasswordUiState.isVerifyEmail,
                resetPasswordUiState.codeTimer,
                changedInput = { resetPasswordViewModel.updateInput(ResetPasswordField.CODE, it) },
                clickedBtn = { resetPasswordViewModel.verifyCode() }
            )

            // 비밀번호 입력
            PasswordBar(
                resetPasswordUiState.password,
                changedInput = { resetPasswordViewModel.updateInput(ResetPasswordField.PASSWORD, it) }
            )

            // 비밀번호 체크 입력
            CheckPasswordBar(resetPasswordUiState.verifyPasswordInput, resetPasswordUiState.isSame, changedInput = { resetPasswordViewModel.updateInput(ResetPasswordField.VERIFY, it) })

            Spacer(modifier = Modifier.weight(1f))

            // 비밀번호 재설정 버튼
            Button(
                onClick = { resetPasswordViewModel.resetPassword() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PointColor,
                    contentColor = Color.White
                ),
                enabled = resetPasswordUiState.isVerifyEmail && resetPasswordUiState.password.isNotBlank() && resetPasswordUiState.isSame
            ) {
                Text(
                    text = "비밀번호 재설정",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )
            }
        }
    }
}
