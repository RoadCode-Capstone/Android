package com.example.roadcode.ui.screen
//
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip // ✅ clip을 쓰기 위한 import 추가

// 커스텀 색상 정의
val PrimaryColor = Color(0xFF2B3440)
val PointColor = Color(0xFFFFC107)

@Composable
fun SignUpSuccessScreen(userName: String = "OOO", point: Int = 0) {
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
                text = "${userName}님,",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "가입을 축하드립니다!",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp)) // ✅ shape 적용
                    .background(Color.White)         // ✅ 배경색 분리 적용
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User Icon",
                    tint = PrimaryColor,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = userName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$point 포인트",
                    fontSize = 14.sp,
                    color = PrimaryColor
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "지금 바로 로드코드를 사용해보세요!",
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = { /* TODO: 로그인 화면으로 이동 */ },
                colors = ButtonDefaults.buttonColors(containerColor = PointColor),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "로그인 하기",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
