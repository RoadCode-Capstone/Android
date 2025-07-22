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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.data.model.RoadmapDTO
import com.example.roadcode.ui.theme.BackGrayColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.viewmodel.RoadmapViewModel
import kotlinx.coroutines.launch
import kotlin.math.round

/* 로드맵 조회 화면 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapScreen(navController: NavController, roadmapViewModel: RoadmapViewModel) {
    val scope = rememberCoroutineScope()
    var isDrawerOpen by remember { mutableStateOf(false) }  // 드로어 열림 여부 변수

    // 로드맵 데이터 예시
    val roadmapInfo = RoadmapDTO.roadmapData(
        1,
        "로드맵 제목",
        "language",
        "python",
        "",
        RoadmapDTO.roadmapProblem(
            774,
            62,
            3,
            "IN_PROGRESS"
        )
    )

    // 로드맵 문제 데이터 예시
    val problemsInfo = listOf(
        RoadmapDTO.roadmapProblem(771, 2195, 0, "COMPLETED"),
        RoadmapDTO.roadmapProblem(772, 14, 1, "COMPLETED"),
        RoadmapDTO.roadmapProblem(773, 20, 2, "COMPLETED"),
        RoadmapDTO.roadmapProblem(774, 62, 3, "IN_PROGRESS"),
        RoadmapDTO.roadmapProblem(775, 98, 4, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(776, 10, 5, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(777, 209, 6, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(778, 289, 7, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(779, 331, 8, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(780, 15, 9, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(781, 45, 10, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(782, 122, 11, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(783, 11, 12, "NOT_STARTED"),
        RoadmapDTO.roadmapProblem(784, 23, 13, "NOT_STARTED")
    )

    val progress = ((roadmapInfo.currentProblem.order + 1).toFloat() / problemsInfo.size) * 100 // 진행도
    val formatted_progress = String.format("%.1f", progress)

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = roadmapInfo.title,
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
                    },
                    actions = {
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
                                append("${3}") // 남은 수 계산 필요 (일일 학습 목표 어디서 조회하지?)
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
                                text = "${formatted_progress}%",
                                fontSize = 17.sp,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                                color = PrimaryColor
                            )

                            Spacer(modifier = Modifier.padding(bottom = 12.dp))

                            StepBar(problemInfo = problemsInfo) // 단계 바 출력
                        }

                        Spacer(modifier = Modifier.width(30.dp))

                        Column(modifier = Modifier.fillMaxHeight()) {
                            problemPreview( // 문제 미리보기 출력
                                modifier = Modifier.weight(1f),
                                "A. 선거",
                                "여러분이 아는 바와 같이, 여름 정보학교의 대부분의 학생들과 교사들은 대체로 베를란드에 거주하고 있다. 그곳의 부패가 상당히 만연해 있기 때문에, 다음과 같은 이야기는 드물지 않다. 선거가 다가오고 있다. 유권자의 수와 정당의 수는 각각 <b>n</b>과 <b>m</b>이다. 각 유권자가 어떤 정당에 투표할 것인지 알고 있다. 그러나 특정 금액의 돈을 주면 쉽게 투표를 변경할 수 있다. 특히, <b>i</b>-번째 유권자에게 <b>c<sub>i</sub></b> 바이트코인을 주면, 그가 원하는 다른 정당에 투표하도록 요청할 수 있다. 베를란드 통합당은 통계 조사를 수행하기로 결정하였다. 당의 승리를 보장하기 위해 필요한 최소한의 바이트코인 수를 계산해야 한다. 정당이 선거에서 승리하기 위해서는 다른 어떤 정당보다도 엄격히 더 많은 표를 받아야 한다."
                            )

                            Row(
                                modifier = Modifier.padding(top = 20.dp, bottom = 40.dp)
                            ) {
                                Button( // 시작하기 버튼
                                    onClick = {
                                        /* 문제 풀이 화면으로 이동 */
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

                        drawerItem("문제 추가하기", onClick = {
                            /* 문제 추가 기능*/
                        })

                        Spacer(modifier = Modifier.height(10.dp))

                        drawerItem("로드맵 포기하기", onClick = {
                            /* 로드맵 포기 기능*/
                        })
                    }
                }
            }
        }
    }
}

/* 드로어 아이템 */
@Composable
fun drawerItem(text: String, onClick: () -> Unit) {
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
fun problemPreview(modifier: Modifier, title: String, description: String) {
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
fun StepBar(problemInfo: List<RoadmapDTO.roadmapProblem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 40.dp),
        reverseLayout = true,    // 아래에서부터 보여주기
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 10.dp)
    ) {
        itemsIndexed(problemInfo) { idx, problem ->
            StepCircle(idx, problem.status)
            if (idx != problemInfo.size - 1) {
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
fun StepCircle(idx: Int, status: String) {
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
            .border(width = 3.dp, shape = CircleShape, color = PrimaryColor),
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
