package com.example.roadcode.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.BackGrayColor
//
@Composable
fun FindPasswordScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("인증번호가 틀렸습니다") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryColor)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "비밀번호 찾기",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        "이메일을 입력하세요",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BackGrayColor,
                    unfocusedContainerColor = BackGrayColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { /* 인증번호 발송 */ },
                colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text(
                    "발송",
                    style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                placeholder = {
                    Text(
                        "인증번호를 입력하세요",
                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BackGrayColor,
                    unfocusedContainerColor = BackGrayColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { /* 인증번호 확인 */ },
                colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Text(
                    "확인",
                    style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                )
            }
        }

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.White,
                fontSize = 12.sp
            ),
            modifier = Modifier.align(Alignment.Start)
        )
    }
}
