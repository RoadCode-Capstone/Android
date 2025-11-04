package com.example.roadcode.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.roadcode.R
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor

enum class LoadingOverlayName { DEFAULT, RESULT }

@Composable
fun LoadingOverlay(isLoading: Boolean, name: LoadingOverlayName) {
    if (!isLoading) return

    when (name) {
        LoadingOverlayName.DEFAULT ->   DefaultLoadingOverlay()
        LoadingOverlayName.RESULT ->    ResultLoadingOverlay()
    }
}

/* 기본 로딩 화면 */
@Composable
fun DefaultLoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)) // 반투명 검정 배경
            .clickable(enabled = false) {}              // 뒤 화면 터치 막기
            .zIndex(1f),                               // 다른 컴포넌트 위로
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.White,                       // 로딩바 색상
            strokeWidth = 4.dp
        )
    }
}

/* 레벨 테스트 결과 로딩 화면 & 풀이 제출 로딩 화면 */
@Composable
fun ResultLoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .zIndex(10f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 100.dp)
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 상단 텍스트
            Text(
                text = "채점중...",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                color = PrimaryColor
            )

            // 로딩바
            CircularProgressIndicator(
                color = PointColor,
                strokeWidth = 5.dp,
                modifier = Modifier.size(60.dp).padding(top = 30.dp)
            )

            // 마스코트 이미지
            Image(
                painter = painterResource(id = R.drawable.mascot_basic),
                contentDescription = "로딩 마스코트",
                modifier = Modifier
                    .size(180.dp)
                    .padding(top = 20.dp)
            )
        }
    }
}