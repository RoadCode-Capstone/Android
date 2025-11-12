package com.example.roadcode.view

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.util.rememberOnce
import com.example.roadcode.viewmodel.PointViewModel
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointScreen(navController: NavController, pointViewModel: PointViewModel) {
    val uiItems by pointViewModel.uiItems.collectAsState()
    val yearMonth by pointViewModel.yearMonth.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "포인트 내역",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp)
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    MonthBar(yearMonth = yearMonth, onClickPrevMonth = { pointViewModel.prevMonth() }, onClickNextMonth = { pointViewModel.nextMonth() })
                }

                uiItems.forEachIndexed { idx, item ->
                    when (item) {
                        is PointViewModel.PointUiItem.DayHeader -> {
                            item {
                                if (idx != 0) {
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 15.dp),
                                        color = Color.LightGray,
                                        thickness = 0.5.dp
                                    )
                                }
                                DayBar(date = item.date, dayTotal = item.dayTotal)
                                Spacer(modifier = Modifier.height(15.dp))
                            }
                        }
                        is PointViewModel.PointUiItem.Entry -> {
                            item {
                                PointBar(name = item.name, amount = item.amount)
                                Spacer(modifier = Modifier.height(15.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

/* 월 출력 바 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MonthBar(yearMonth: YearMonth, onClickPrevMonth: () -> Unit, onClickNextMonth: () -> Unit) {
    val nowYear = YearMonth.now(java.time.ZoneId.of("Asia/Seoul")).year
    val printYearMonth = if (nowYear == yearMonth.year) "${yearMonth.monthValue}월" else "${yearMonth.year}년 ${yearMonth.monthValue}월"    // 현재 년도와 같으면 월만 출력, 다르면 년월 출력

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MonthMoveButton(icon = Icons.Default.ChevronLeft, onClick = { onClickPrevMonth() })

        Text(
            text = printYearMonth,
            fontSize = 17.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
        )

        MonthMoveButton(icon = Icons.Default.ChevronRight, onClick = { onClickNextMonth() })
    }
}

/* 일 출력 바 */
@Composable
fun DayBar(date: Int, dayTotal: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${date}일",
            fontSize = 16.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
        )
    }
}

/* 포인트 출력 바 */
@Composable
fun PointBar(name: String, amount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 16.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
        )

        Text(
            text = "+${amount}",
            fontSize = 16.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
        )
    }
}