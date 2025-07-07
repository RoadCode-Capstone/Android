package com.example.roadcode.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.ui.theme.BackGrayColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.view.component.algorithmItem
import com.example.roadcode.viewmodel.RoadmapPlanViewModel
import com.example.roadcode.viewmodel.TagViewModel

/* 학습 계획 설정 화면 (언어) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapPlanLanguageScreen(navController: NavController) {
    val roadmapViewModel: RoadmapPlanViewModel = hiltViewModel()
    var selectedLanguage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "학습 계획 설정",
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
                modifier = Modifier.fillMaxSize().padding(bottom = 160.dp),
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
                    onClick = { selectedLanguage = "JAVA" },
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(0.5.dp, PrimaryColor),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackGrayColor
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
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button( // Python 버튼
                    onClick = { selectedLanguage = "python" },
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(0.5.dp, PrimaryColor),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackGrayColor
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
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button( // C 버튼
                    onClick = { selectedLanguage = "c" },
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(0.5.dp, PrimaryColor),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackGrayColor
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
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 30.dp, bottom = 40.dp)
            ) {
                Button( // 다음 버튼
                    onClick = {
                        roadmapViewModel.setSelectedLanguage(selectedLanguage)
                        navController.navigate("plan_type")
                    },
                    enabled = selectedLanguage != null, // 언어 선택했을 때만 활성화
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
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
            }
        }
    }
}

/* 학습 계획 설정 화면 (학습 유형) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapPlanTypeScreen(navController: NavController) {
    val roadmapViewModel: RoadmapPlanViewModel = hiltViewModel()
    var selectedType by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "학습 계획 설정",
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
                modifier = Modifier.fillMaxSize().padding(bottom = 160.dp),
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
                    onClick = { selectedType = "Language" },
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(0.5.dp, PrimaryColor),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackGrayColor
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "언어",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button( // 알고리즘 버튼
                    onClick = { selectedType = "Algorithm" },
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(0.5.dp, PrimaryColor),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BackGrayColor
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "알고리즘",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 30.dp, bottom = 40.dp)
            ) {
                Button( // 다음 버튼
                    onClick = {
                        roadmapViewModel.setSelectedType(selectedType)
                        if (selectedType == "Language") navController.navigate("plan_goal")
                        else navController.navigate("plan_algorithm")
                    },
                    enabled = selectedType != null,
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
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
            }
        }
    }
}

/* 학습 계획 설정 화면 (알고리즘) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapPlanAlgorithmScreen(navController: NavController) {
    val roadmapViewModel: RoadmapPlanViewModel = hiltViewModel()
    val tagViewModel: TagViewModel = hiltViewModel()
    val tags by tagViewModel.tags.collectAsState()

    var selectedAlgorithm by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "학습 계획 설정",
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
                modifier = Modifier.fillMaxSize().padding(bottom = 160.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = "학습할 알고리즘을 선택하세요",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                )

                Spacer(modifier = Modifier.height(50.dp))

                // 알고리즘 버튼 출력
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(tags) { tag ->
                        algorithmItem(name = tag) {
                            selectedAlgorithm = tag
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }

            Row(
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 30.dp, bottom = 40.dp)
            ) {
                Button( // 다음 버튼
                    onClick = {
                        roadmapViewModel.setSelectedAlgorithm(selectedAlgorithm)
                        navController.navigate("plan_goal")
                    },
                    enabled = selectedAlgorithm != null,
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
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
            }
        }
    }
}

/* 학습 계획 설정 화면 (일일 학습 목표) */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapPlanGoalScreen(navController: NavController) {
    val roadmapViewModel: RoadmapPlanViewModel = hiltViewModel()
    var selectedGoal by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "학습 계획 설정",
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
                modifier = Modifier.fillMaxSize().padding(bottom = 160.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = "일일 학습 목표를 선택하세요",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                )

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button( // 1 버튼
                        onClick = { selectedGoal = 1 },
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        border = BorderStroke(0.5.dp, PrimaryColor),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BackGrayColor
                        )
                    ) {
                        Box(
                            modifier = Modifier.size(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "1",
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button( // 2 버튼
                        onClick = { selectedGoal = 2 },
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        border = BorderStroke(0.5.dp, PrimaryColor),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BackGrayColor
                        )
                    ) {
                        Box(
                            modifier = Modifier.size(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "2",
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button( // 3 버튼
                        onClick = { selectedGoal = 3 },
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        border = BorderStroke(0.5.dp, PrimaryColor),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BackGrayColor
                        )
                    ) {
                        Box(
                            modifier = Modifier.size(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "3",
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button( // 4 버튼
                        onClick = { selectedGoal = 4 },
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        border = BorderStroke(0.5.dp, PrimaryColor),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BackGrayColor
                        )
                    ) {
                        Box(
                            modifier = Modifier.size(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "4",
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button( // 5 버튼
                        onClick = { selectedGoal = 5 },
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        border = BorderStroke(0.5.dp, PrimaryColor),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BackGrayColor
                        )
                    ) {
                        Box(
                            modifier = Modifier.size(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "5",
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 30.dp, bottom = 40.dp)
            ) {
                Button( // 다음 버튼
                    onClick = {
                        roadmapViewModel.setSelectedGoal(selectedGoal)
                        /* 다음 화면 이동 */
                    },
                    enabled = selectedGoal != null,
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
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
            }
        }
    }
}