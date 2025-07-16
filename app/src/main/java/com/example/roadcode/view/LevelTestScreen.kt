package com.example.roadcode.view

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.data.model.LevelTestDTO
import com.example.roadcode.data.model.ProblemDTO
import com.example.roadcode.ui.theme.BackGrayColor
import com.example.roadcode.ui.theme.LineColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.viewmodel.LevelTestViewModel
import com.example.roadcode.viewmodel.RoadmapPlanViewModel
import kotlinx.coroutines.delay
import org.json.JSONObject

/* 레벨 테스트 준비 화면 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelTestReadyScreen(navController: NavController, roadmapViewModel: RoadmapPlanViewModel, levelTestViewModel: LevelTestViewModel) {
    val plan by roadmapViewModel.plan.collectAsState()

    val infos = listOf(
        "제한 시간은 2시간이에요.",
        "사용 언어는 '${plan.selectedLanguage}'에요.",
        "테스트 내용은 '${if (plan.selectedAlgorithm == null) "언어" else plan.selectedAlgorithm}'에요.",
        "제한 시간이 종료되거나, 종료 버튼을 누르면 종료돼요."
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "레벨 테스트",
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
                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = "레벨 테스트를 시작할게요",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                )

                Spacer(modifier = Modifier.height(50.dp))

                // 주의사항 출력
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .background(color = BackGrayColor)
                        .border(width = 0.5.dp, color = Color.Black),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    
                    Text(
                        text = "주의사항",
                        fontSize = 17.sp,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()    // 높이를 내용만큼만
                            .padding(horizontal = 30.dp)
                    ) {
                        infos.forEach { info ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(text = "•", modifier = Modifier.padding(end = 8.dp))
                                Text(
                                    text = info,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 30.dp, end = 30.dp, bottom = 40.dp)
            ) {
                Button( // 시작하기 버튼
                    onClick = {
                        // 레벨 테스트 생성
                        val request = LevelTestDTO.createRequest(plan.selectedType!!, plan.selectedLanguage!!, if (plan.selectedType == "언어") null else plan.selectedAlgorithm)
                        levelTestViewModel.createLevelTest(request)
//                        levelTestViewModel.getLevelTestProblems(listOf(584, 2000, 237, 62, 70))
                        navController.navigate("level_test")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "시작하기",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                }
            }
        }
    }
}

/* 레벨 테스트 화면 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelTestScreen(navController: NavController, roadmapViewModel: RoadmapPlanViewModel, levelTestViewModel: LevelTestViewModel) {
    var remainingSeconds by remember { mutableStateOf(120 * 60) }   // 남은 시간 (120분부터 시작)

    LaunchedEffect(Unit) {
        // 남은 시간 감소 (0 되면 풀이 제출)
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        }
    }

    val plan by roadmapViewModel.plan.collectAsState()                              // 로드맵 학습 계획
    val problemInfos by levelTestViewModel.problemInfos.collectAsState()            // 문제 정보 리스트
    val codes by levelTestViewModel.codes.collectAsState()                          // 작성한 코드 맵
    val formattedTime = String.format("%d:%02d", remainingSeconds / 60, remainingSeconds % 60)
    var problemIdx by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "레벨 테스트",
                        fontSize = 18.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                },
                navigationIcon = {
                    Button( // 종료 버튼
                        onClick = {
                                  /* 풀이 제출 */
                        },
                        modifier = Modifier
                            .height(40.dp)
                            .padding(start = 15.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "종료",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                        )
                    }
                },
                actions = {
                    Text( // 남은 시간 출력
                        text = formattedTime,
                        fontSize = 16.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                        modifier = Modifier.padding(end = 20.dp)
                    )
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
                // 문제 출력
                if (problemInfos.isNotEmpty()) {
                    ProblemPager(problemInfos[problemIdx],
                        plan.selectedLanguage!!,
                        codes[problemIdx]!!,
                        onCodeChanged = { code ->
                            levelTestViewModel.updateCode(problemIdx, code)
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 30.dp, bottom = 40.dp)
            ) {
                Button( // 다음 버튼
                    onClick = {
                        if (problemIdx == 4) {
                            /* 문제 제출 */
                            levelTestViewModel.submitLevelTest(plan.selectedLanguage!!)
                            navController.navigate("level_result")
                        } else {
                            problemIdx++
                        }
                    },
                    modifier = Modifier
                        .width(90.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (problemIdx == 4) "제출" else "다음",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                }
            }
        }
    }
}

/* 문제 출력 및 풀이 화면 */
@SuppressLint("SetJavaScriptEnabled")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProblemPager(problemInfos: List<String>, language: String, initCode: String, onCodeChanged: (String) -> Unit) { // [제목, 설명, 입력 설명, 출력 설명, 시간제한, 메모리제한], 사용 언어, 초기 코드, 코드 변경 시 동작
    val pagerState = rememberPagerState(pageCount = { 2 })
    val keys = listOf("제목", "문제 설명", "입력 설명", "출력 설명", "시간 제한", "메모리 제한")
    var currentCode by remember(initCode) { mutableStateOf(initCode) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
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
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        itemsIndexed(problemInfos) { idx, info ->
                            if (idx == 0) {
                                Text(
                                    text = info,
                                    fontSize = 17.sp,
                                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                                    color = PrimaryColor
                                )

                                Spacer(modifier = Modifier.height(40.dp))
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
                        modifier = Modifier.fillMaxSize()
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

/* 레벨 테스트 결과 화면 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelTestResultScreen(navController: NavController, levelTestViewModel: LevelTestViewModel) {
    val levelTestResults by levelTestViewModel.levelTestResults.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "레벨 테스트 결과",
                        fontSize = 18.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
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
                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = "레벨 테스트 결과로\n맞춤 로드맵을 생성했어요",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light)),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp  // 줄 간격
                )

                Spacer(modifier = Modifier.height(50.dp))

                // 레벨 테스트 문제별 결과 출력
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .background(color = BackGrayColor)
                        .border(width = 0.5.dp, color = Color.Black),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()    // 높이를 내용만큼만
                            .padding(horizontal = 30.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))

                        levelTestResults?.result?.forEachIndexed { idx, result ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp, vertical = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "문제 ${idx + 1}",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                                )

                                Text(
                                    text = if (result) "정답" else "오답",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                                )
                            }

                            if (idx != 4) {
                                Divider(modifier = Modifier, color = LineColor, thickness = 0.5.dp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 30.dp, end = 30.dp, bottom = 40.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button( // 바로 학습하러 가기 버튼
                        onClick = {
                                  /* 해당 로드맵의 로드맵 조회 화면으로 이동 */
                        },
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
                            text = "바로 학습하러 가기",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                        )
                    }
                    
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    Button( // 홈으로 돌아가기 버튼
                        onClick = {
                                  /* 홈 화면으로 이동 */
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "홈으로 돌아가기",
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
