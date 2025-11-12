package com.example.roadcode.view

import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.util.rememberOnce
import com.example.roadcode.viewmodel.WriteReviewViewModel
import org.json.JSONObject

/* 풀이 선택 화면 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSubmissionScreen(navController: NavController, writeReviewViewModel: WriteReviewViewModel) {
    val reviewUiState by writeReviewViewModel.reviewUiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "리뷰 작성",
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
                    .padding(horizontal = 30.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "리뷰를 작성할 풀이를 선택해 주세요",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light)),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(reviewUiState.submissions) { submissionInfo ->
                            SubmissionInfoItem(
                                submissionInfo,
                                onClick = {
                                    writeReviewViewModel.setSubmissionInfo(submissionInfo)
                                    navController.navigate("review_write") // 리뷰 작성 화면으로 이동
                                }
                            )
                        }
                    }

                    Button(
                        onClick = { navController.popBackStack("roadmap_list", inclusive = false) },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 30.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PointColor,
                            contentColor = Color.White
                        ),
                        enabled = if (reviewUiState.complete >= 2) true else false
                    ) {
                        Text(
                            text = if (reviewUiState.complete >= 2) "계속 로드맵 진행하기" else "성공 인정까지 남은 리뷰 ${2 - reviewUiState.complete}개",
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

/* 풀이 정보 출력 아이템 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionInfoItem(submissionInfo: SubmissionDTO.OtherSubmissionsData, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 0.5.dp, color = PrimaryColor),
        onClick = { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = submissionInfo.nickname,
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )

                Text(
                    text = "사용언어: ${submissionInfo.language}",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                )
            }

            // 코드
            CodeViewItem(submissionInfo)
        }
    }
}

/* 리뷰 작성 화면 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReviewScreen(navController: NavController, writeReviewViewModel: WriteReviewViewModel) {
    val context = LocalContext.current

    val reviewUiState by writeReviewViewModel.reviewUiState.collectAsState()

    LaunchedEffect(Unit) {
        writeReviewViewModel.navigateBack.collect {
            navController.popBackStack()
        }
    }

    LaunchedEffect(Unit) {
        writeReviewViewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            writeReviewViewModel.updateReview("")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "리뷰 작성",
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
                    .padding(horizontal = 30.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // 코드 출력
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "풀이 코드",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        CodeViewItem(submissionInfo = reviewUiState.submissionInfo)
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 15.dp))

                // 리뷰 작성 필드
                Box(
                    modifier = Modifier.weight(2f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "리뷰 내용",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        OutlinedTextField(
                            modifier = Modifier.fillMaxSize(),
                            value = reviewUiState.review,
                            onValueChange = { writeReviewViewModel.updateReview(it) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                cursorColor = PrimaryColor,
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            )
                        )
                    }
                }

                // 리뷰 등록 버튼
                Button(
                    onClick = {
                        // 등록 성공하면 리뷰 작성할 풀이 선택 화면으로 이동, 작성한 리뷰 수 +1
                        writeReviewViewModel.submitReview()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 30.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "리뷰 등록하기",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                }
            }
        }
    }
}

/* 코드 조회 아이템 */
@Composable
fun CodeViewItem(submissionInfo: SubmissionDTO.OtherSubmissionsData) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true

            setLayerType(View.LAYER_TYPE_SOFTWARE, null)

            isFocusable = false
            isFocusableInTouchMode = false
            isEnabled = false

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    evaluateJavascript("setMode('${submissionInfo.language}');", null)
                    val safeCode = JSONObject.quote(submissionInfo.sourceCode)
                    evaluateJavascript("setCode($safeCode);", null)
                }
            }

            loadUrl("file:///android_asset/editor.html")
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { webView }
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> webView.onResume()
                Lifecycle.Event.ON_PAUSE  -> webView.onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            try {
                webView.onPause()
                (webView.parent as? ViewGroup)?.removeView(webView)
            } catch (_: Exception) { }
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
