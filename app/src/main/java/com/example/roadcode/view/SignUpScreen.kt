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
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryColor)
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

            SignUpTextField(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = "이메일을 입력하세요",
                helperText = "사용할 수 있는 ID입니다 / 이미 가입된 이메일입니다",
                trailing = {
                    Button(
                        onClick = { /* 이메일 발송 */ },
                        colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            "발송",
                            style = MaterialTheme.typography.labelLarge.copy(color = PrimaryColor)
                        )
                    }
                }
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
                onClick = { /* 재설정 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    "재설정",
                    style = MaterialTheme.typography.labelLarge.copy(color = PrimaryColor)
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
    trailing: @Composable (() -> Unit)? = null
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                )
            },
            trailingIcon = trailing,
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
