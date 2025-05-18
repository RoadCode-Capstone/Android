package com.example.myapplication

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roadcode.R


val PrimaryColor = Color(0xFF2B3440)   // 주 색상
val PointColor = Color(0xFFFF2C53D)    // 포인트 색
val BackGrayColor = Color(0xFFFFF9F9F9) // 요소 배경색

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryColor)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "로그인",
            fontSize = 28.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("이메일을 입력하세요", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
                    contentDescription = "Email Icon",
                    tint = PointColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BackGrayColor,
                unfocusedContainerColor = BackGrayColor,
                disabledContainerColor = BackGrayColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = PrimaryColor,
                unfocusedTextColor = PrimaryColor
            ),
            singleLine = true,
            textStyle = TextStyle(color = PrimaryColor)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("비밀번호를 입력하세요", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_lock_lock),
                    contentDescription = "Password Icon",
                    tint = PointColor
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BackGrayColor,
                unfocusedContainerColor = BackGrayColor,
                disabledContainerColor = BackGrayColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = PrimaryColor,
                unfocusedTextColor = PrimaryColor
            ),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            textStyle = TextStyle(color = PrimaryColor)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "비밀번호 찾기",
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { /* 회원가입 */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "회원가입", color = Color.White)
            }

            Button(
                onClick = { println("로그인 시도: 이메일=$email, 비밀번호=$password") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "로그인", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(PointColor, shape = RoundedCornerShape(8.dp))
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_dialog_email),
                        contentDescription = "Kakao Icon",
                        tint = PrimaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "로그인", color = PrimaryColor, fontWeight = FontWeight.Bold)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(BackGrayColor, shape = RoundedCornerShape(8.dp))
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_login_image), // 구글 로고 필요
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(Color(0xFF03C75A), shape = RoundedCornerShape(8.dp))
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "N", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    }
}
