package com.example.roadcode.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.ui.theme.BackGrayColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.viewmodel.LevelTestViewModel
import com.example.roadcode.viewmodel.RoadmapPlanViewModel
import kotlinx.coroutines.delay

/* 레벨 테스트 준비 화면 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelTestReadyScreen(navController: NavController, roadmapViewModel: RoadmapPlanViewModel, levelTestViewModel: LevelTestViewModel) {
    val plan by roadmapViewModel.plan.collectAsState()

    val infos = listOf(
        "제한 시간은 2시간이에요.",
        "사용 언어는 '${plan.selectedLanguage}'에요.",
        "테스트 내용은 '${if (plan.selectedAlgorithm == null) "언어" else plan.selectedAlgorithm}'에요.",
        "제한 시간이 종료되거나, 종료 버튼을 누르면 종료돼요."
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "레벨 테스트",
                        fontSize = 18.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 160.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = "레벨 테스트를 시작할게요",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                )

                Spacer(modifier = Modifier.height(50.dp))

                // 주의사항 출력
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .background(color = BackGrayColor)
                        .border(width = 0.5.dp, color = Color.Black),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    
                    Text(
                        text = "주의사항",
                        fontSize = 17.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()    // 높이를 내용만큼만
                            .padding(horizontal = 30.dp)
                    ) {
                        infos.forEach { info ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(text = "•", modifier = Modifier.padding(end = 8.dp))
                                Text(
                                    text = info,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                                )
                            }

                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 30.dp, end = 30.dp, bottom = 40.dp)
            ) {
                Button( // 시작하기 버튼
                    onClick = {
                        // 레벨 테스트 생성
                        val request = LevelTestDTO.createRequest(plan.selectedType!!, plan.selectedLanguage!!, if (plan.selectedType == "언어") null else plan.selectedAlgorithm)
//                        levelTestViewModel.createLevelTest(request)
                        navController.navigate("level_test")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "시작하기",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                }
            }
        }
    }
}

/* 레벨 테스트 화면 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelTestScreen(navController: NavController, levelTestViewModel: LevelTestViewModel) {
    var remainingSeconds by remember { mutableStateOf(120 * 60) }   // 남은 시간 (120분부터 시작)

    LaunchedEffect(Unit) {
        // 남은 시간 감소 (0 되면 풀이 제출)
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        }
    }

    val levelTestIds by levelTestViewModel.levelTestIds.collectAsState()
    val formattedTime = String.format("%d:%02d", remainingSeconds / 60, remainingSeconds % 60)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "레벨 테스트",
                        fontSize = 18.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                },
                navigationIcon = {
                    Button( // 종료 버튼
                        onClick = {
                                  /* 풀이 제출 */
                        },
                        modifier = Modifier.height(40.dp).padding(start = 15.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "종료",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                        )
                    }
                },
                actions = {
                    Text( // 남은 시간 출력
                        text = formattedTime,
                        fontSize = 16.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                        modifier = Modifier.padding(end = 20.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 160.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(36.dp))

                // 문제 출력
            }
/*
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 30.dp, end = 30.dp, bottom = 40.dp)
            ) {
                Button( // 시작하기 버튼
                    onClick = {
                        // 레벨 테스트 생성
                        val request = LevelTestDTO.createRequest(
                            plan.selectedType!!,
                            plan.selectedLanguage!!,
                            if (plan.selectedType == "언어") null else plan.selectedAlgorithm
                        )
                        levelTestViewModel.createLevelTest(request)
                        navController.navigate("level_test")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "시작하기",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                }
            }*/
        }
    }
}

/* 레벨 테스트 결과 화면 */
@Composable
fun LevelTestResultScreen(navController: NavController) {

}
