package com.example.roadcode.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.view.component.BottomNavigationBar
import com.example.roadcode.viewmodel.AttendanceViewModel
import com.example.roadcode.viewmodel.CalendarViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, attendanceViewModel: AttendanceViewModel, calendarViewModel: CalendarViewModel) {
    val yearMonth by calendarViewModel.yearMonth.collectAsState()
    val year = yearMonth.year
    val month = yearMonth.monthValue

    LaunchedEffect(Unit) {
        attendanceViewModel.checkAttendance()
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
                .padding(horizontal = 30.dp)
        ) {
            /* TODO: 한 달 출석 횟수 출력 */

            Calendar(calendarViewModel, year, month)

            /* TODO: 한 달 풀이 성공한 문제 출력 */
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(calendarViewModel: CalendarViewModel, year: Int, month: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        MonthBar(month = month, onClickPrevMonth = { calendarViewModel.prevMonth() }, onClickNextMonth = { calendarViewModel.nextMonth() })
        WeekDayBar()
        DateGrid(year, month)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            color = PrimaryColor,
            thickness = 0.5.dp
        )
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
fun DateGrid(year: Int, month: Int) {
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
                DayItem(date = date++)
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
                        DayItem(date = date++)
                    }
                    else {
                        Spacer(modifier = Modifier.size(30.dp))
                    }
                }
            }
        }
    }
}

/* 날짜 출력 아이템  */
@Composable
fun DayItem(date: Int) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(
                color = PointColor, /* TODO: 출석 유형별 색상 구분 */
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