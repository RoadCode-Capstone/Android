package com.example.roadcode.view

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.ui.theme.LineColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.util.rememberOnce
import com.example.roadcode.view.component.CustomAlertDialog
import com.example.roadcode.viewmodel.ProblemViewModel
import com.example.roadcode.viewmodel.RoadmapViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject

/* 문제 풀이 화면 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProblemScreen(navController: NavController, problemViewModel: ProblemViewModel, roadmapViewModel: RoadmapViewModel) {
    val roadmapInfo by roadmapViewModel.roadmapInfo.collectAsState()
    val problemInfo by problemViewModel.problemInfo.collectAsState()
    val code by problemViewModel.code.collectAsState()
    val result by problemViewModel.result.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(result) {
        if (result.isNotBlank()) {
            result.let {
                snackbarHostState.showSnackbar(it)
                problemViewModel.clearResult()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (!problemInfo.isEmpty()) problemInfo[0] else "",
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
                actions = {
                    IconButton(
                        onClick = { /* TODO: 제출한 풀이 기록 조회 버튼 */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Article,
                            contentDescription = "제출한 풀이 기록 조회 버튼",
                            tint = PrimaryColor
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(30.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ProblemPager( // 문제 출력
                    problemInfo,
                    roadmapInfo.language,
                    code,
                    onCodeChanged = { problemViewModel.updateCode(it) })
            }

            Button( // 제출하기 버튼
                onClick = { problemViewModel.submitSolution(roadmapInfo.language) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PointColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "제출하기",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )
            }
        }
    }
}

/* 문제 출력 및 풀이 화면 */
@SuppressLint("SetJavaScriptEnabled")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProblemPager(problemInfos: List<String>, language: String, initCode: String, onCodeChanged: (String) -> Unit) { // 문제 정보, 사용 언어, 초기 코드, 코드 변경 시 동작
    val pagerState = rememberPagerState(pageCount = { 2 })
    val keys = listOf("제목", "문제 설명", "입력 설명", "출력 설명", "시간 제한", "메모리 제한")
    var currentCode by remember(initCode) { mutableStateOf(initCode) }

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
        ) { page ->
            when (page) {
                0 -> { // 문제 정보 조회 화면
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 20.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        itemsIndexed(problemInfos) { idx, info ->
                            if (idx == 0) {

                            } else {
                                Text(   // 부제목 출력
                                    text = keys[idx],
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                                    color = PrimaryColor
                                )

                                Spacer(modifier = Modifier.height(15.dp))

                                if (idx == 1 || idx == 2 || idx == 3) {
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
                                            val spanned = Html.fromHtml(info, Html.FROM_HTML_MODE_COMPACT)
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
                                } else {
                                    Text(
                                        text = info,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_light)),
                                        color = PrimaryColor
                                    )

                                }

                                if (idx != keys.size - 1) {
                                    Divider(modifier = Modifier.padding(vertical = 20.dp), color = LineColor, thickness = 0.5.dp)
                                }
                            }
                        }
                    }
                }

                1 -> { // 풀이 화면 (코드 에디터)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 20.dp)
                    ) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                WebView(context).apply {
                                    settings.javaScriptEnabled = true
                                    settings.allowFileAccess = true
                                    settings.domStorageEnabled = true

                                    isFocusableInTouchMode = true
                                    requestFocus()

                                    webViewClient = object : WebViewClient() {
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)

                                            // 페이지 로드 완료 후 언어 모드 설정
                                            evaluateJavascript("setMode('$language');", null)

                                            // 저장된 코드 초기값 넣기
                                            val safeCode = JSONObject.quote(currentCode)
                                            evaluateJavascript("setCode($safeCode);", null)
                                        }
                                    }

                                    // 안드로이드로 코드 자동 전송 (editor.html에서 setInterval 돌리고 있음)
                                    addJavascriptInterface(object {
                                        @JavascriptInterface
                                        fun onCodeSubmit(code: String) {
                                            currentCode = code
                                            onCodeChanged(code)
                                        }
                                    }, "Android")

                                    loadUrl("file:///android_asset/editor.html")
                                }
                            }
                        )

                    }
                }
            }
        }
    }
}