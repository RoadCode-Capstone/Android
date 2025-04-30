package com.example.roadcode.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import com.example.roadcode.R

/* 학습 계획 설정 화면 (언어) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapPlanLanguageScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "학습 계획 설정",
                        fontSize = 18.sp,
                        color = Color(0xFF2B3440),
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { /* 뒤로 가기 동작 */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기 버튼",
                            tint = Color(0xFF2B3440)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "학습할 언어를 선택하세요",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button( // JAVA 버튼
                onClick = { /* JAVA 선택 */ },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(0.5.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF9F9F9),
                    contentColor = Color(0xFF2B3440)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logos_java),
                        contentDescription = "Java 로고"
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = "JAVA",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button( // Python 버튼
                onClick = { /* Python 선택 */ },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(0.5.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF9F9F9),
                    contentColor = Color(0xFF2B3440)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logos_python),
                        contentDescription = "Python 로고"
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = "Python",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button( // C 버튼
                onClick = { /* C 선택 */ },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(0.5.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF9F9F9),
                    contentColor = Color(0xFF2B3440)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logos_c),
                        contentDescription = "C 로고"
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(
                        text = "C",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                    )
                }
            }

            Spacer(modifier = Modifier.height(360.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Button( // 다음 버튼
                    onClick = { /* 다음 버튼 */ },
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF2C53D),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "다음",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                }
                
                Spacer(modifier = Modifier.width(30.dp))
            }
        }
    }
}

/* 학습 계획 설정 화면 (학습 유형) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapPlanTypeScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "학습 계획 설정",
                        fontSize = 18.sp,
                        color = Color(0xFF2B3440),
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { /* 뒤로 가기 동작 */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기 버튼",
                            tint = Color(0xFF2B3440)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "학습 유형을 선택하세요",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button( // 언어 버튼
                onClick = { /* 언어 선택 */ },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(0.5.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF9F9F9),
                    contentColor = Color(0xFF2B3440)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "언어",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button( // 알고리즘 버튼
                onClick = { /* 알고리즘 선택 */ },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(0.5.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF9F9F9),
                    contentColor = Color(0xFF2B3440)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "알고리즘",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                    )
                }
            }

            Spacer(modifier = Modifier.height(430.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Button( // 다음 버튼
                    onClick = { /* 다음 버튼 */ },
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF2C53D),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "다음",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                }

                Spacer(modifier = Modifier.width(30.dp))
            }
        }
    }
}

/* 학습 계획 설정 화면 (알고리즘) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapPlanAlgorithmScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "학습 계획 설정",
                        fontSize = 18.sp,
                        color = Color(0xFF2B3440),
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { /* 뒤로 가기 동작 */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기 버튼",
                            tint = Color(0xFF2B3440)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "학습할 알고리즘을 선택하세요",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button( // 스택/큐 버튼
                onClick = { /* 스택/큐 선택 */ },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(0.5.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF9F9F9),
                    contentColor = Color(0xFF2B3440)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "스택/큐",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button( // DFS/BFS 버튼
                onClick = { /* DFS/BFS 선택 */ },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(0.5.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF9F9F9),
                    contentColor = Color(0xFF2B3440)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DFS/BFS",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button( // 탐욕법 버튼
                onClick = { /* 탐욕법 선택 */ },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(0.5.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF9F9F9),
                    contentColor = Color(0xFF2B3440)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "탐욕법",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button( // 트리 버튼
                onClick = { /* 트리 선택 */ },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(0.5.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF9F9F9),
                    contentColor = Color(0xFF2B3440)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "트리",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                    )
                }
            }

            Spacer(modifier = Modifier.height(290.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Button( // 다음 버튼
                    onClick = { /* 다음 버튼 */ },
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF2C53D),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "다음",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                }

                Spacer(modifier = Modifier.width(30.dp))
            }
        }
    }
}