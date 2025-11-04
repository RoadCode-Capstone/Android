package com.example.roadcode.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.util.rememberOnce
import com.example.roadcode.viewmodel.LoginField
import com.example.roadcode.viewmodel.LoginUiState
import com.example.roadcode.viewmodel.LoginViewModel
import com.example.roadcode.viewmodel.RegisterField
import com.example.roadcode.viewmodel.RegisterUiState
import com.example.roadcode.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay

/* 회원가입 화면 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, registerViewModel: RegisterViewModel) {
    val context = LocalContext.current

    val navigateBack by registerViewModel.navigateBack.collectAsState()
    val registerUiState by registerViewModel.registerUiState.collectAsState()

    LaunchedEffect(Unit) {
        registerViewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            registerViewModel.setFalseNavigateBack()
            navController.popBackStack()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            registerViewModel.init()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "회원가입",
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
                RegisterCard(registerUiState, registerViewModel)
            }
        }
    }
}

/* 회원가입 카드 */
@Composable
fun RegisterCard(registerUiState: RegisterUiState, registerViewModel: RegisterViewModel) {
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
            // 이메일 입력 & 이메일 중복 확인 버튼
            EmailBar(registerUiState.email, registerUiState.verifiedEmail, changedInput = { registerViewModel.updateInput(RegisterField.EMAIL, it) }, clickedBtn = { registerViewModel.verifyEmailRegister() })

            // 인증코드 입력 & 인증코드 발송 버튼(재발송) & 5분 타이머 & 인증코드 확인 버튼
            VerifyCodeBar(registerUiState.code, registerUiState.isVerifyEmail, registerUiState.codeTimer, changedInput = { registerViewModel.updateInput(RegisterField.CODE, it) }, clickedBtn = { registerViewModel.verifyCode() })

            // 비밀번호 입력
            PasswordBar(registerUiState.password, changedInput = { registerViewModel.updateInput(RegisterField.PASSWORD, it) })

            // 닉네임 입력 & 닉네임 중복 확인 버튼
            NicknameBar(registerUiState, registerViewModel)

            Spacer(modifier = Modifier.weight(1f))

            // 회원가입 버튼
            Button(
                onClick = { registerViewModel.register() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PointColor,
                    contentColor = Color.White
                ),
                enabled = registerUiState.isVerifyEmail && registerUiState.isVerifyNickname && registerUiState.password.isNotBlank()
            ) {
                Text(
                    text = "회원가입",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )
            }
        }
    }
}

/* 이메일 입력 & 인증코드 전송 버튼 바 */
@Composable
fun EmailBar(
    email: String,
    verifiedEmail: String,
    changedInput: (String) -> Unit,
    clickedBtn: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "이메일",
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(0.62f),
                value = email,
                onValueChange = { changedInput(it) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Button( // 인증코드 발송 버튼 버튼 - 중복되는 이메일이면 오류 출력, 눌렸으면 재전송으로 바꾸기
                onClick = { clickedBtn() },
                modifier = Modifier.weight(0.38f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PointColor,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (verifiedEmail.isNotBlank() && email == verifiedEmail) "인증코드 재전송" else "인증코드 받기",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )
            }
        }
    }
}


/* 인증코드 입력 & 5분 타이머 & 인증코드 확인 버튼 바 */
@Composable
fun VerifyCodeBar(
    code: String,
    isVerifyEmail: Boolean,
    codeTimer: Int,
    changedInput: (String) -> Unit,
    clickedBtn: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "인증코드",
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(0.8f),
                value = code,
                onValueChange = { changedInput(it) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                trailingIcon = {
                    if (isVerifyEmail) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "이메일 검증 완료 버튼",
                            tint = Color.Green
                        )
                    }
                    else {
                        Text(
                            text = if (codeTimer == 0) "" else String.format(
                                "%02d:%02d",
                                codeTimer / 60,
                                codeTimer % 60
                            ),
                            fontSize = 12.sp,
                            color = if (codeTimer > 0) Color.Red else Color.Gray,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                }
            )

            Button( // 인증코드 확인 버튼
                onClick = { clickedBtn() },
                modifier = Modifier.weight(0.2f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PointColor,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "확인",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )
            }
        }
    }
}

/* 비밀번호 입력 바 */
@Composable
fun PasswordBar(
    password: String,
    changedInput: (String) -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "비밀번호",
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { changedInput(it) },
                singleLine = true,
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = image,
                            contentDescription = "비밀번호 보기"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            )
        }
    }
}

/* 닉네임 입력 & 중복 검사 버튼 바 */
@Composable
private fun NicknameBar(
    registerUiState: RegisterUiState,
    registerViewModel: RegisterViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "닉네임",
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(0.7f),
                value = registerUiState.nickname,
                onValueChange = { registerViewModel.updateInput(RegisterField.NICKNAME, it) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    errorBorderColor = MaterialTheme.colorScheme.error
                ),
                trailingIcon = {
                    if (registerUiState.isVerifyNickname) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "이메일 검증 완료 버튼",
                            tint = Color.Green
                        )
                    }
                }
            )

            Button( // 닉네임 중복 확인 버튼
                onClick = { registerViewModel.checkDuplicatedNickname() },
                modifier = Modifier.weight(0.3f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PointColor,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "중복 확인",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )
            }
        }
    }
}