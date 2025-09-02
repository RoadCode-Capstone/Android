package com.example.roadcode.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current

    val userInfoUiState by userViewModel.userInfoUiState.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "회원 정보",
                        fontSize = 18.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            userViewModel.setEditMode(false)
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기 버튼",
                            tint = PrimaryColor
                        )
                    }
                },
                actions = {
                    if (!userInfoUiState.isEdit) {
                        IconButton(
                            onClick = { userViewModel.setEditMode(isEdit = true) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "회원 정보 수정 버튼",
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
                    .padding(bottom = 40.dp)
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!userInfoUiState.isEdit) {
                    InfoBar(name = "이메일", data = userInfoUiState.userInfo.email)
                    InfoBar(name = "닉네임", data = userInfoUiState.userInfo.nickname)
                }
                else {
                    InfoBar(name = "이메일", data = userInfoUiState.userInfo.email)
                    InfoEditBar(name = "닉네임", data = userInfoUiState.nicknameInput, onValueChange = { userViewModel.updateNicknameInput(it) })

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 100.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button( // 수정 취소 버튼
                            onClick = { userViewModel.setEditMode(false) },
                            modifier = Modifier
                                .weight(0.5f)
                                .height(50.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryColor,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "취소",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))
                        
                        Button( // 회원 정보 수정 버튼
                            onClick = { userViewModel.editUserInfo() },
                            modifier = Modifier
                                .weight(0.5f)
                                .height(50.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PointColor,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "수정",
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

/* 정보 출력 바 */
@Composable
fun InfoBar(name: String, data: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 16.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
        )

        Text(
            text = data,
            fontSize = 16.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
        )
    }
}

/* 정보 수정 바 */
@Composable
fun InfoEditBar(name: String, data: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 16.sp,
            color = PrimaryColor,
            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
        )

        TextField(
            value = data,
            onValueChange = onValueChange
        )
    }
}