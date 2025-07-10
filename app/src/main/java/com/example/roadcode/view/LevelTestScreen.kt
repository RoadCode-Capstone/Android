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
import com.example.roadcode.ui.theme.BackGrayColor
import com.example.roadcode.ui.theme.LineColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.viewmodel.LevelTestViewModel
import com.example.roadcode.viewmodel.RoadmapPlanViewModel
import kotlinx.coroutines.delay

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
//                        levelTestViewModel.createLevelTest(request)
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

    val plan by roadmapViewModel.plan.collectAsState()
    val levelTestIds by levelTestViewModel.levelTestIds.collectAsState()
    val levelTestProblems by levelTestViewModel.levelTestProblems.collectAsState()
    val formattedTime = String.format("%d:%02d", remainingSeconds / 60, remainingSeconds % 60)
    var problemIdx = 0;

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
                val problemInfos = LevelTestDTO.getResponse(1, 1, "A", "두 수의 합", 1,
                    "Pizano는 각 <b>a</b>의 <b>n</b> 개의 타워로 구성된 배열을 만들었다. 각 타워는 <b>a<sub>i</sub> >= 0</b> 블록으로 이루어져 있다.\n" +
                            "\n" +
                            "Pizano는 타워를 무너뜨려서 다음 <b>a<sub>i</sub></b> 개의 타워가 <b>1</b> 만큼 자라게 할 수 있다. 즉, 그는 <b>a<sub>i</sub></b> 요소를 가져와서 다음 <b>a<sub>i</sub></b> 요소를 하나씩 증가시키고, 그 후 <b>a<sub>i</sub></b>를 <b>0</b>으로 설정할 수 있다. 타워에서 떨어진 블록은 사라진다. Pizano가 <b>0</b> 블록이 있는 타워를 무너뜨리면 아무 일도 일어나지 않는다.\n" +
                            "\n" +
                            "Pizano는 모든 <b>n</b> 개의 타워를 어떤 순서로든 한 번씩 정확히 무너뜨리고 싶어 한다. 즉, <b>1</b>부터 <b>n</b>까지의 각 <b>i</b>에 대해, 그는 위치 <b>i</b>에 있 는 타워를 정확히 한 번 무너뜨릴 것이다.\n" +
                            "\n" +
                            "게다가, 결과적으로 얻어진 타워 높이 배열은 오름차순이어야 한다. 이는 그가 모든 <b>n</b> 개의 타워를 무너뜨린 후, 어떤 <b>i < j</b>에 대해 위치 <b>i</b>의 타워가  위치 <b>j</b>의 타워보다 더 높지 않아야 함을 의미한다.\n" +
                            "당신은 결과적으로 얻어진 타워 높이 배열의 최대 <b>MEX</b>를 출력해야 한다.\n" +
                            "배열의 <b>MEX</b>는 배열에 존재하지 않는 가장 작은 양수 정수이다.",
                    "각 테스트는 여러 개의 테스트 케이스를 포함한다. 첫 번째 줄에는 테스트 케이스의 수 <b>t</b> (<b>1 <= t <= 10<sup>4</sup></b>)가 있다. 테스트 케이스의 설명은 다음과 같다.\n" +
                            "\n" +
                            "각 테스트 케이스의 첫 번째 줄에는 정수 <b>n</b> (<b>1 <= n <= 10<sup>5</sup></b>) — 타워의 수가 있다.\n" +
                            "\n" +
                            "각 테스트 케이스의 두 번째 줄에는 <b>n</b> 개의 정수 — 타워의 초기 높이 <b>a<sub>1</sub>, ..., a<sub>n</sub></b> (<b>0 <= a<sub>i</sub> <= 10<sup>9</sup></b>)가  있다.\n" +
                            "\n" +
                            "모든 테스트 케이스에 대한 <b>n</b>의 합은 <b>10<sup>5</sup></b>를 초과하지 않는다고 보장된다.",
                    "각 테스트 케이스에 대해, 최종 배열의 최대 <b>MEX</b>를 나타내는 단일 정수를 출력하라.",
                    "1s", "256MB", "", listOf(""))
                val problemInfoList = listOf(problemInfos.name, problemInfos.description, problemInfos.inputDescription, problemInfos.outputDescription, problemInfos.timeLimit, problemInfos.memoryLimit)
                ProblemPager(problemInfoList, plan.selectedLanguage!!, onCodeChanged = { code ->
                    levelTestViewModel.updateCode(problemIdx, code)
                })
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 30.dp, bottom = 40.dp)
            ) {
                Button( // 다음 버튼 (마지막 문제일 때는 제출로 변경) 안바뀐다 왜지??
                    onClick = {
                        if (problemIdx == 4) {
                            /* 문제 제출 */
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
fun ProblemPager(problemInfos: List<String>, language: String, onCodeChanged: (String) -> Unit) { // 제목, 설명, 입력 설명, 출력 설명, 시간제한, 메모리제한
    val pagerState = rememberPagerState(pageCount = { 2 })
    val keys = listOf("제목", "문제 설명", "입력 설명", "출력 설명", "시간 제한", "메모리 제한")

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
                                    AndroidView(factory = { context ->
                                        TextView(context).apply {
                                            val spanned = Html.fromHtml(info, Html.FROM_HTML_MODE_COMPACT)
                                            val spannable = SpannableStringBuilder(spanned)

                                            // subscript span 찾아서 relative size span으로 덮어쓰기 (sub 태그 사용 시 줄 간격 커지는 것 방지)
                                            spannable.getSpans(0, spannable.length, SubscriptSpan::class.java).forEach { span ->
                                                val start = spannable.getSpanStart(span)
                                                val end = spannable.getSpanEnd(span)
                                                spannable.setSpan(RelativeSizeSpan(0.7f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                            }

                                            text = spannable

                                            textSize = 16f
                                            setTextColor(PrimaryColor.toArgb())
                                            typeface = resources.getFont(R.font.spoqahansansneo_light)
                                            setLineSpacing(10f, 1.3f)    // 줄 간격
                                        }
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

                                    // 페이지 로드 완료 후 언어 모드 설정
                                    webViewClient = object : WebViewClient() {
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            super.onPageFinished(view, url)
                                            evaluateJavascript("setMode('$language');", null)
                                        }
                                    }

                                    // 안드로이드로 코드 자동 전송 (editor.html에서 setInterval 돌리고 있음)
                                    addJavascriptInterface(object {
                                        @JavascriptInterface
                                        fun onCodeSubmit(code: String) {
                                            Log.d("CODE_SUBMIT", "받은 코드: $code")
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
@Composable
fun LevelTestResultScreen(navController: NavController) {

}
