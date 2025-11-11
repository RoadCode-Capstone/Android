package com.example.roadcode.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.view.component.BottomNavigationBar
import com.example.roadcode.viewmodel.AttendanceType
import com.example.roadcode.viewmodel.AttendanceUiState
import com.example.roadcode.viewmodel.AttendanceViewModel
import com.example.roadcode.viewmodel.CalendarViewModel
import com.example.roadcode.viewmodel.ViewReviewViewModel
import java.time.LocalDate
import java.time.YearMonth

/* 홈(캘린더) 화면 */
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, attendanceViewModel: AttendanceViewModel, calendarViewModel: CalendarViewModel, viewReviewViewModel: ViewReviewViewModel) {
    val yearMonth by calendarViewModel.yearMonth.collectAsState()
    val year = yearMonth.year
    val month = yearMonth.monthValue

    val attendanceUiState by attendanceViewModel.attendanceUiState.collectAsState()

    LaunchedEffect(yearMonth) {
        attendanceViewModel.getMonthAttendanceInfo(yearMonth)
        attendanceViewModel.getMonthSubmissions(yearMonth)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "홈",
                        fontSize = 18.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("mypage") }
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

                // 한 달 출석 횟수
                Text(
                    buildAnnotatedString {
                        append("이번 달에 총 ")
                        withStyle(
                            style = SpanStyle(
                                color = PointColor,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                            )
                        ) {
                            append("${attendanceUiState.attendanceCnt}")
                        }
                        append("번 출석했어요!")
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                )

                // 캘린더
                Calendar(calendarViewModel, year, month, attendanceUiState)

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    color = PrimaryColor,
                    thickness = 1.dp
                )

                // 한 달 풀이 성공한 문제 출력
                MonthSubmissions(navController, attendanceUiState.submissions, viewReviewViewModel)
            }
        }
    }
}

/* 한 달 풀이 목록 출력 아이템 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthSubmissions(navController: NavController, submissions: List<SubmissionDTO.MySubmissionsData>, viewReviewViewModel: ViewReviewViewModel) {
//    var expandedStates = remember { mutableStateMapOf<Long, Boolean>() }    // 문제별 풀이 목록 열림 여부 (키: problemId)
    var expandedStates = remember { mutableStateMapOf<String, Boolean>() }    // 문제별 풀이 목록 열림 여부 (키: date+problemId)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
        ) {
            Text(
                text = "날짜",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.2f)
            )

            Text(
                text = "문제 제목",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                modifier = Modifier.weight(0.8f)
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            color = PrimaryColor,
            thickness = 0.5.dp
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 30.dp)
        ) {
            if (submissions.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "풀이 내역이 없어요",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else {
                submissions.forEach { submission ->
                    itemsIndexed(submission.submissionDetails) { idx, submissionInfo ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(end = 10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                modifier = Modifier.weight(0.2f),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // 날짜 출력 (같은 날짜는 한 번만 출력)
                                val day = LocalDate.parse(submission.date).dayOfMonth.toString().padStart(2, '0')

                                Text(
                                    text = if (idx == 0) day else if (submission.date != submissions[idx - 1].date) day else "",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                                )
                            }

                            Column(
                                modifier = Modifier.weight(0.8f),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            expandedStates["${submission.date} + ${submissionInfo.problemId}"] =
                                                !(expandedStates["${submission.date} + ${submissionInfo.problemId}"]
                                                    ?: false)
                                        },
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val rotation by animateFloatAsState(
                                        targetValue = if (expandedStates.getOrDefault(
                                                "${submission.date} + ${submissionInfo.problemId}",
                                                false
                                            )
                                        ) 90f else 0f
                                    )

                                    // 문제 제목 출력
                                    Text(
                                        text = submissionInfo.problemName,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                                    )

                                    Icon(
                                        imageVector = Icons.Default.ArrowForwardIos,
                                        contentDescription = "풀이 목록 열림/닫힘 아이콘",
                                        modifier = Modifier
                                            .size(12.dp)
                                            .rotate(rotation),
                                        tint = PrimaryColor
                                    )
                                }

                                // 풀이 목록 출력
                                AnimatedVisibility(
                                    visible = expandedStates.getOrDefault(
                                        "${submission.date} + ${submissionInfo.problemId}",
                                        false
                                    )
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // 풀이 정보 출력
                                            val submissionName =
                                                "${if (submissionInfo.isSuccess) "[정답]" else "[오답]"} 풀이 시도 ${idx}"

                                            Text(
                                                text = submissionName,
                                                fontSize = 14.sp,
                                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                                            )

                                            Button(
                                                onClick = {
                                                    viewReviewViewModel.setIds(submissionInfo.problemId, submissionInfo.submissionId)
                                                    viewReviewViewModel.getProblem()
                                                    viewReviewViewModel.getSubmission()
                                                    viewReviewViewModel.getReviewComment()
                                                    navController.navigate("review_view")   // 문제, 풀이, 리뷰 조회 화면으로 이동
                                                },
                                                modifier = Modifier.height(30.dp),
                                                shape = RoundedCornerShape(20.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = PointColor,
                                                    disabledContentColor = Color.White
                                                ),
                                                enabled = submissionInfo.isSuccess,
                                                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 5.dp)
                                            ) {
                                                Text(
                                                    text = "리뷰 보기",
                                                    fontSize = 14.sp,
                                                    color = PrimaryColor,
                                                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

/* 캘린더 아이템 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(calendarViewModel: CalendarViewModel, year: Int, month: Int, attendanceUiState: AttendanceUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        MonthBar(month = month, onClickPrevMonth = { calendarViewModel.prevMonth() }, onClickNextMonth = { calendarViewModel.nextMonth() })
        WeekDayBar()
        DateGrid(year, month, attendanceUiState)
    }
}

/* 현재 월 출력 및 이동 바 */
@Composable
fun MonthBar(month: Int, onClickPrevMonth: () -> Unit, onClickNextMonth: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MonthMoveButton(icon = Icons.Default.ChevronLeft, onClick = { onClickPrevMonth() })

        Text(
            text = "${month}월",
            fontSize = 18.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
        )

        MonthMoveButton(icon = Icons.Default.ChevronRight, onClick = { onClickNextMonth() })
    }
}

/* 월 이동 버튼 */
@Composable
fun MonthMoveButton(icon: ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = { onClick() }
    ) {
        Icon(imageVector = icon, contentDescription = "월 이동 버튼", tint = PrimaryColor)
    }
}

/* 요일 출력 바 */
@Composable
fun WeekDayBar() {
    val days = listOf("일", "월", "화", "수", "목", "금", "토")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            Box(
                modifier = Modifier.size(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    fontSize = 16.sp,
                    color = PrimaryColor,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                )
            }
        }
    }
}

/* 전체 날짜 출력 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateGrid(year: Int, month: Int, attendanceUiState: AttendanceUiState) {
    val typeMap = attendanceUiState.attendanceTypeMap
    val startWeekDay = LocalDate.of(year, month, 1).dayOfWeek   // 월의 시작 요일 (월=1, 화=2, ..., 일=7)
    val endDayOfMonth = YearMonth.of(year, month).lengthOfMonth()          // 월의 마지막 날짜
    val total = startWeekDay.value % 7 + endDayOfMonth  // 총 칸 수
    val rows = (total + 6) / 7                          // 주 수
    var date = 1

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 첫째 주: 1일 시작 요일까지 빈 칸 생성 후 날짜 출력
            val emptyCnt = startWeekDay.value % 7
            repeat(emptyCnt) {
                Spacer(modifier = Modifier.size(30.dp))
            }

            for (idx in 1..(7 - emptyCnt)) {
                DayItem(date = date, type = typeMap[date.toString()] ?: AttendanceType.NOT_LOGIN)
                date++
            }
        }

        for (week in 1 until rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(7) {
                    if (date <= endDayOfMonth) {
                        DayItem(date = date, type = typeMap[date.toString()] ?: AttendanceType.NOT_LOGIN)
                        date++
                    }
                    else {
                        Spacer(modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
    }
}

/* 날짜 출력 아이템 */
@Composable
fun DayItem(date: Int, type: AttendanceType) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(
                color = PointColor.copy(alpha = when (type) {
                    // 출석 유형별 색상 구분 (일일 학습 목표 O: 100%, 일일 학습 목표 X 로그인 O: 50%, 로그인 X: 0%)
                    AttendanceType.PERFECT      -> 1f
                    AttendanceType.NOT_GOAL     -> 0.5f
                    AttendanceType.NOT_LOGIN    -> 0f
                }),
                shape = RoundedCornerShape(5.dp)
            )
            .border(width = 1.dp, color = PrimaryColor, shape = RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${date}",
            fontSize = 18.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
        )
    }
}