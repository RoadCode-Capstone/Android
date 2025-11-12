package com.example.roadcode.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.data.model.PointDTO
import com.example.roadcode.ui.theme.BackGrayColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.view.component.BottomNavigationBar
import com.example.roadcode.viewmodel.RankingViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(navController: NavController, rankingViewModel: RankingViewModel) {
    val myRank by rankingViewModel.myRank.collectAsState()  // 사용자 순위
    val ranks by rankingViewModel.ranks.collectAsState()    // 전체 순위 목록
    val myRankInfo by rankingViewModel.myRankInfo.collectAsState()  // 사용자 순위 정보
    val topPercent by rankingViewModel.topPercent.collectAsState()  // 상위 퍼센트

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "순위",
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
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
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

                Text(
                    text = buildAnnotatedString {
                        append("현재 ")

                        withStyle(
                            style = SpanStyle(
                                color = PointColor,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                            )
                        ) {
                            append("상위 ${topPercent}%")
                        }

                        append("에요")
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light)),
                    color = PrimaryColor
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (myRankInfo != null) {
                    MyRankingItem(myRankInfo!!, Modifier.padding(horizontal = 5.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = BackGrayColor)
                        .border(width = 0.5.dp, color = PrimaryColor),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(ranks) { idx, rankInfo ->
                        RankingItem(rankInfo)

                        if (idx != ranks.size - 1) {
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                color = PrimaryColor.copy(alpha = 0.5f),
                                thickness = 0.5.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

/* 내 순위 출력 박스 */
@Composable
fun MyRankingItem(rankInfo: PointDTO.RankData, modifier: Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(5.dp))
            .background(color = BackGrayColor)
            .border(width = 0.5.dp, color = PrimaryColor, shape = RoundedCornerShape(5.dp))
            .padding(vertical = 15.dp, horizontal = 35.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${rankInfo.rank}",
            fontSize = 18.sp,
            color = if (rankInfo.rank in 1..3) PointColor else PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rankInfo.nickname,
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )

            Text(
                text = "${rankInfo.totalPoint}",
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )
        }
    }
}

/* 순위 목록 아이템 */
@Composable
fun RankingItem(rankInfo: PointDTO.RankData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 40.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${rankInfo.rank}",
            fontSize = 18.sp,
            color = if (rankInfo.rank in 1..3) PointColor else PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rankInfo.nickname,
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )

            Text(
                text = "${rankInfo.totalPoint}",
                fontSize = 16.sp,
                color = PrimaryColor,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )
        }
    }
}