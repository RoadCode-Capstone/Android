package com.example.roadcode.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.util.rememberOnce
import com.example.roadcode.viewmodel.DeleteUserViewModel
import com.example.roadcode.viewmodel.PasswordField

/* 회원 탈퇴 화면 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteUserScreen(navController: NavController, deleteUserViewModel: DeleteUserViewModel) {
    val context = LocalContext.current

    val deleteUserUiState by deleteUserViewModel.deleteUserUiState.collectAsState()

    LaunchedEffect(Unit) {
        deleteUserViewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "계정 탈퇴",
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 160.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "현재 비밀번호를 입력하세요.",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                )

                Spacer(modifier = Modifier.height(50.dp))

                PasswordTextField(
                    label = "현재 비밀번호",
                    data = deleteUserUiState.currentPasswordInput,
                    onValueChange = { deleteUserViewModel.updatePassword(it) }
                )

                Spacer(modifier = Modifier.height(100.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            deleteUserViewModel.verifyPassword()
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PointColor,
                            contentColor = Color.White
                        ),
                        enabled = if (deleteUserUiState.currentPasswordInput.isNotBlank()) true else false
                    ) {
                        Text(
                            text = "탈퇴",
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