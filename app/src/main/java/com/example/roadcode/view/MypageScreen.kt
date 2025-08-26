package com.example.roadcode.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.runtime.remember
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
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.view.component.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MypageScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "마이페이지",
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
                    .padding(bottom = 40.dp)
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MypageBar(text = "포인트")
                MypageItem(text = "포인트 내역", onClick = { /* TODO: 포인트 내역 화면으로 이동 */ })
                Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)
                
                MypageBar(text = "회원 정보")
                MypageItem(text = "조회 및 수정", onClick = { /* TODO: 회원 정보 화면으로 이동 */ })
                MypageItem(text = "비밀번호 변경", onClick = { /* TODO: 비밀번호 변경 화면으로 이동 */ })
                Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.LightGray)

                MypageBar(text = "계정")
                MypageItem(text = "로그아웃", onClick = { /* TODO: 로그아웃 기능 */ })
                MypageItem(text = "탈퇴", onClick = { /* TODO: 탈퇴 기능 */ })
            }
        }
    }
}

/* 마이페이지 바 */
@Composable
fun MypageBar(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
        )
    }
}

/* 마이페이지 아이템 */
@Composable
fun MypageItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) { onClick() }
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
        )

        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = ">",
            tint = PrimaryColor
        )
    }
}