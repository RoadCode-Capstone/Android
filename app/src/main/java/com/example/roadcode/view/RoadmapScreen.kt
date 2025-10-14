package com.example.roadcode.view

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.ui.theme.BackGrayColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.util.rememberOnce
import com.example.roadcode.view.component.CustomAlertDialog
import com.example.roadcode.viewmodel.ProblemViewModel
import com.example.roadcode.viewmodel.RoadmapViewModel

/* 로드맵 조회 화면 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapScreen(navController: NavController, roadmapViewModel: RoadmapViewModel, problemViewModel: ProblemViewModel) {
    val scope = rememberCoroutineScope()
    var isDrawerOpen by remember { mutableStateOf(false) }      // 드로어 열림 여부 변수
    var showGiveUpDialog by remember { mutableStateOf(false) }  // 팝업창 열림 여부 변수

    val roadmapInfo by roadmapViewModel.roadmapInfo.collectAsState()        // 로드맵 정보
    val problems by roadmapViewModel.problems.collectAsState()              // 로드맵 문제 목록
    val problemInfo by roadmapViewModel.problemInfo.collectAsState()        // 문제 정보
    val problemIdx by roadmapViewModel.problemIdx.collectAsState()          // 출력할 문제 인덱스 (초기값: 현재 풀어야 하는 문제 인덱스)
    val progress by roadmapViewModel.progress.collectAsState()              // 달성률
    val roadmapStatus by roadmapViewModel.roadmapStatus.collectAsState()    // 로드맵 상태

    if (showGiveUpDialog) {
        CustomAlertDialog(
            showDialog = showGiveUpDialog,
            text = "학습 로드맵을 포기하겠습니까?",
            onConfirm = { roadmapViewModel.giveUpRoadmap() },
            onDismiss = { showGiveUpDialog = false }
        )
    }

    if (roadmapInfo != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = roadmapInfo!!.title,
                                fontSize = 18.sp,
                                color = PrimaryColor,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = rememberOnce { navController.popBackStack() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "뒤로 가기 버튼",
                                    tint = PrimaryColor
                                )
                            }
                        },
                        actions = {
                            if (roadmapStatus != "GAVE_UP") {
                                IconButton(
                                    onClick = { isDrawerOpen = true } // 로드맵 관련 메뉴 드로어 열기
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "로드맵 관련 메뉴 버튼",
                                        tint = PrimaryColor
                                    )
                                }
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
                            .padding(horizontal = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = buildAnnotatedString {
                                append("일일 목표 달성까지 앞으로 ")

                                withStyle(
                                    style = SpanStyle(
                                        color = PointColor,
                                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                                    )
                                ) {
                                    append("${3}") /* TODO: 일일 학습 목표까지 남은 문제 수 계산 필요 */
                                }

                                append("문제")
                            },
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light)),
                            color = PrimaryColor
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text( // 달성률 출력
                                    text = "${progress}%",
                                    fontSize = 17.sp,
                                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                                    color = PrimaryColor
                                )

                                Spacer(modifier = Modifier.padding(bottom = 12.dp))

                                if (problems.isNotEmpty()) {    // 단계 바 출력
                                    StepBar(
                                        problems,
                                        onClick = { idx ->
                                            roadmapViewModel.setProblemIdx(idx)
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(30.dp))

                            Column(modifier = Modifier.fillMaxHeight()) {
                                if (problemInfo != null) {
                                    ProblemPreview( // 문제 미리보기 출력
                                        modifier = Modifier.weight(1f),
                                        title = problemInfo!!.name,
                                        description = problemInfo!!.description
                                    )
                                }

                                Row(
                                    modifier = Modifier.padding(top = 20.dp, bottom = 40.dp)
                                ) {
                                    Button( // 시작하기 버튼
                                        onClick = {
                                            problemViewModel.getProblem(problems[problemIdx].problemId)
                                            navController.navigate("problem")
                                        },
                                        enabled = if (roadmapStatus != "GAVE_UP") true else false,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(50.dp),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (roadmapStatus != "GAVE_UP") PointColor else Color.Gray,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(
                                            text = "학습 시작하기",
                                            fontSize = 16.sp,
                                            color = Color.White,
                                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isDrawerOpen) { // 드로어가 열렸을 때 배경 어둡게 처리
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable( // 터치 효과 제거 (클릭 시 진해지는)
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { isDrawerOpen = false } // 드로어 밖 클릭 시 드로어 닫기
                )
            }

            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                AnimatedVisibility( // 로드맵 메뉴 관리 드로어
                    visible = isDrawerOpen,
                    enter = slideInHorizontally(initialOffsetX = { it }),
                    exit = slideOutHorizontally(targetOffsetX = { it })
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(300.dp),
                        shadowElevation = 8.dp,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 40.dp)
                        ) {
                            Text(
                                text = "로드맵 관리",
                                fontSize = 18.sp,
                                color = PrimaryColor,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                                modifier = Modifier.padding(start = 10.dp)
                            )

                            Spacer(modifier = Modifier.height(50.dp))

                            DrawerItem("문제 추가하기", onClick = {
                                /* TODO: 문제 추가 기능 */
                            })

                            Spacer(modifier = Modifier.height(10.dp))

                            DrawerItem("로드맵 포기하기", onClick = { showGiveUpDialog = true })
                        }
                    }
                }
            }
        }
    }
}

/* 드로어 아이템 */
@Composable
fun DrawerItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                indication = rememberRipple(
                    bounded = true
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
        )
    }
}

/* 문제 미리보기 출력 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProblemPreview(modifier: Modifier, title: String, description: String) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = BackGrayColor)
            .border(width = 1.dp, color = PrimaryColor, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 30.dp, vertical = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
            color = PrimaryColor
        )
        
        Spacer(modifier = Modifier.height(40.dp))

        LazyColumn(
            modifier = modifier
        ) {
            item {
                AndroidView(
                    factory = { context ->
                        TextView(context).apply {
                            textSize = 16f
                            setTextColor(PrimaryColor.toArgb())
                            typeface = resources.getFont(R.font.spoqahansansneo_light)
                            setLineSpacing(10f, 1.3f)   // 줄 간격
                        }
                    },
                    update = { textView ->
                        val spanned = Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
                        val spannable = SpannableStringBuilder(spanned)

                        // subscript span 찾아서 relative size span으로 덮어쓰기 (sub 태그 사용 시 줄 간격 커지는 것 방지)
                        spannable.getSpans(0, spannable.length, SubscriptSpan::class.java).forEach { span ->
                            val start = spannable.getSpanStart(span)
                            val end = spannable.getSpanEnd(span)
                            spannable.setSpan(RelativeSizeSpan(0.7f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }

                        textView.text = spannable
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/* 단계 바 */
@Composable
fun StepBar(problems: List<RoadmapDTO.RoadmapProblem>, onClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 40.dp),
        reverseLayout = true,    // 아래에서부터 보여주기
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 10.dp)
    ) {
        itemsIndexed(problems) { idx, problem ->
            StepCircle(idx, problem.status, onClick = { idx -> onClick(idx) })
            if (idx != problems.size - 1) {
                Box(modifier = Modifier
                    .width(3.dp)
                    .height(50.dp)
                    .background(PrimaryColor))
            }
        }
    }
}

/* 단계 원 출력 */
@Composable
fun StepCircle(idx: Int, status: String, onClick: (Int) -> Unit) {
    val backgroundColor = when (status) {
        "COMPLETED" -> PrimaryColor
        "IN_PROGRESS" -> PointColor
        else -> BackGrayColor   // NOT_STARTED
    }

    val textColor = when (status) {
        "COMPLETED" -> BackGrayColor
        else -> PrimaryColor    // IN_PROGRESS, NOT_STARTED
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(width = 3.dp, shape = CircleShape, color = PrimaryColor)
            .clickable { onClick(idx) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${idx + 1}",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
            color = textColor
        )
    }
}
