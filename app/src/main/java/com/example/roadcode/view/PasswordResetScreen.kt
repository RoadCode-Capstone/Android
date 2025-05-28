package com.example.roadcode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordResetUI(modifier: Modifier = Modifier) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isPasswordValid = password.length >= 8 && password.any { !it.isLetterOrDigit() }
    val isConfirmValid = confirmPassword == password && confirmPassword.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2E3540))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "비밀번호 재설정",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        PasswordField(
            label = "비밀번호를 입력하세요",
            value = password,
            onValueChange = { password = it },
            isValid = isPasswordValid,
            helperText = "사용할 수 있는 비밀번호입니다 / 특수문자가 포함되어야 합니다"
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordField(
            label = "비밀번호 재입력하세요",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            isValid = isConfirmValid,
            helperText = "일치하지 않는 비밀번호입니다 (성공 시에는 안내문 X)"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // 비밀번호 재설정 처리
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCC49)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("재설정", color = Color.Black, fontSize = 16.sp)
        }
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isValid: Boolean,
    helperText: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = null,
            tint = if (isValid) Color.White else Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.width(240.dp)
            )
            Text(
                text = helperText,
                fontSize = 10.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
