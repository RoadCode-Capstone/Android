package com.example.roadcode.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.ui.theme.BackGrayColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.view.component.BottomNavigationBar
import com.example.roadcode.viewmodel.RoadmapViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapListScreen(navController: NavController, roadmapViewModel: RoadmapViewModel) {
    val roadmaps by roadmapViewModel.roadmaps.collectAsState()  // 로드맵 목록
    val progress by roadmapViewModel.progress.collectAsState()  // 달성률
    val status by roadmapViewModel.status.collectAsState()      // 선택한 로드맵 상태

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "로드맵 목록",
                        fontSize = 18.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                },
                actions = {
                    IconButton(
                        onClick = { /* TODO: 마이페이지 화면으로 이동 */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "마이페이지 버튼",
                            tint = PrimaryColor
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("plan_language") },  // 로드맵 생성 화면으로 이동
                containerColor = PointColor,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "로드맵 생성 버튼",
                    tint = Color.White
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
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
                    .padding(bottom = 40.dp)
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StateCheckbox(name = "진행 중", state = "IN_PROGRESS" in status, onChecked = { roadmapViewModel.setStatus("IN_PROGRESS", it) })
                    StateCheckbox(name = "완료", state = "COMPLETED" in status, onChecked = { roadmapViewModel.setStatus("COMPLETED", it) })
                    StateCheckbox(name = "포기", state = "GAVE_UP" in status, onChecked = { roadmapViewModel.setStatus("GAVE_UP", it) })
                }

                Spacer(modifier = Modifier.height(15.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(roadmaps) { roadmapInfo ->
                        RoadmapItem(roadmapInfo, progress, onClick = {
                            roadmapViewModel.setRoadmapId(roadmapInfo.roadmapId)    // 클릭한 로드맵 아이디로 변경
                            roadmapViewModel.setRoadmapStatus(roadmapInfo.status)   // 클릭한 로드맵 상태로 변경
                            navController.navigate("roadmap")                 // 로드맵 조회 화면으로 이동
                        })
                    }
                }
            }
        }
    }
}

/* 로드맵 상태 체크 박스 */
@Composable
fun StateCheckbox(name: String, state: Boolean, onChecked: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onChecked(!state) }
            )
    ) {
        Checkbox(
            checked = state,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = PrimaryColor,
                checkmarkColor = Color.White
            ),
            modifier = Modifier.padding(end = 5.dp)
        )
        Text(
            text = name,
            fontSize = 16.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
        )
    }
}

/* 로드맵 목록 아이템 */
@Composable
fun RoadmapItem(roadmapInfo: RoadmapDTO.roadmapsData, progress: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = BackGrayColor, shape = RoundedCornerShape(10.dp))
            .border(width = 0.5.dp, color = PrimaryColor, shape = RoundedCornerShape(10.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,      // 효과가 퍼지는 영역을 안으로 제한
                    color = PrimaryColor
                ),
                onClick = onClick
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = roadmapInfo.title,
                fontSize = 17.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
            )

            Text(
                text = when(roadmapInfo.status) {
                    "IN_PROGRESS" -> "진행 중"
                    "COMPLETED" -> "완료"
                    "GAVE_UP" -> "포기"
                    else -> "알 수 없음" },
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when(roadmapInfo.status) {
                    "IN_PROGRESS" -> "${progress}%"
                    "COMPLETED" -> "100%"
                    "GAVE_UP" -> ""
                    else -> "" },
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )
        }
    }
}