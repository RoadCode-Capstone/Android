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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.viewmodel.PasswordField
import com.example.roadcode.viewmodel.PasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordScreen(navController: NavController, passwordViewModel: PasswordViewModel) {
    val context = LocalContext.current

    val navigateBack by passwordViewModel.navigateBack.collectAsState()
    val passwordUiState by passwordViewModel.passwordUiState.collectAsState()

    LaunchedEffect(Unit) {
        passwordViewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    if (navigateBack) {
        passwordViewModel.setFalseNavigateBack()
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "비밀번호 변경",
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
                    data = passwordUiState.currentPasswordInput,
                    onValueChange = { passwordViewModel.updatePassword(PasswordField.CURRENT, it) }
                )
                PasswordTextField(
                    label = "새로운 비밀번호",
                    data = passwordUiState.newPasswordInput,
                    onValueChange = { passwordViewModel.updatePassword(PasswordField.NEW, it) }
                )
                PasswordTextField(
                    label = "비밀번호 확인",
                    data = passwordUiState.verifyPasswordInput,
                    onValueChange = { passwordViewModel.updatePassword(PasswordField.VERIFY, it) },
                    isSame = passwordUiState.isSame
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
                            passwordViewModel.verifyPassword()
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PointColor,
                            contentColor = Color.White
                        ),
                        enabled = passwordUiState.isSame && (passwordUiState.currentPasswordInput != "")
                    ) {
                        Text(
                            text = "변경",
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

/* 비밀번호 입력 필드 */
@Composable
fun PasswordTextField(label: String, data: String, onValueChange: (String) -> Unit, isSame: Boolean? = null) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(  // 비밀번호 입력 필드
        value = data,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff

            IconButton(
                onClick = { passwordVisible = !passwordVisible }
            ) {
                Icon(
                    imageVector = image,
                    contentDescription = "비밀번호 보기"
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            errorBorderColor = MaterialTheme.colorScheme.error
        ),
        isError = isSame != null && !isSame,
        supportingText = if (isSame != null && !isSame) {
            {
                Text(
                    text = "입력하신 비밀번호가 일치하지 않습니다.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else null
    )

    Spacer(modifier = Modifier.height(20.dp))
}