package com.example.roadcode.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.BackGrayColor

@Composable
fun SignUpScreen() {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val nickname = remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PrimaryColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "회원가입",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 이메일 입력창과 발송 버튼을 나란히 배치
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // 통일된 높이
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    placeholder = {
                        Text(
                            "이메일을 입력하세요",
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = PrimaryColor
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(BackGrayColor, shape = RoundedCornerShape(12.dp)),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = BackGrayColor,
                        focusedContainerColor = BackGrayColor,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black,
                        cursorColor = PrimaryColor
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { /* 이메일 발송 */ },
                    colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier
                        .height(48.dp)
                ) {
                    Text(
                        "발송",
                        style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                    )
                }
            }

            Text(
                text = "사용할 수 있는 ID입니다 / 이미 가입된 이메일입니다",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                ),
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            SignUpTextField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = "비밀번호를 입력하세요",
                helperText = "사용할 수 있는 비밀번호입니다 / 특수문자가 포함되어야 합니다",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            SignUpTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                placeholder = "비밀번호 재입력하세요",
                helperText = "일치하지 않는 비밀번호입니다 (성공 시에는 안내문 X)",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            SignUpTextField(
                value = nickname.value,
                onValueChange = { nickname.value = it },
                placeholder = "닉네임을 입력하세요"
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* 회원가입 하기 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    "회원가입 하기",
                    style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    helperText: String = "",
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = PrimaryColor
                )
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(BackGrayColor, shape = RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = BackGrayColor,
                focusedContainerColor = BackGrayColor,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                cursorColor = PrimaryColor
            )
        )

        if (helperText.isNotEmpty()) {
            Text(
                text = helperText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}
