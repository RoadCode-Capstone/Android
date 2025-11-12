package com.example.roadcode.view

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.roadcode.R
import com.example.roadcode.data.model.ReviewDTO
import com.example.roadcode.data.model.SubmissionDTO
import com.example.roadcode.ui.theme.LineColor
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor
import com.example.roadcode.util.rememberOnce
import com.example.roadcode.viewmodel.ViewReviewViewModel
import org.json.JSONObject

/* 리뷰 조회 화면 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewReviewScreen(navController: NavController, viewReviewViewModel: ViewReviewViewModel) {
    val context = LocalContext.current

    val viewReviewUiState by viewReviewViewModel.viewReviewUiState.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("문제내용", "작성코드", "리뷰답글")

    LaunchedEffect(Unit) {
        viewReviewViewModel.toast.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewReviewViewModel.initComment()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "리뷰 조회",
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
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = PointColor
                        )
                    }
                ) {
                    tabs.forEachIndexed { idx, title ->
                        Tab(
                            selected = selectedTabIndex == idx,
                            onClick = { selectedTabIndex = idx },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                                )
                            },
                            selectedContentColor = PointColor,
                            unselectedContentColor = PrimaryColor
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(color = Color.White)
                        .weight(1f)
                        .padding(horizontal = 30.dp, vertical = 20.dp)
                ) {
                    when (selectedTabIndex) {
                        0 -> ProblemInfoScreen(viewReviewUiState.problemInfo)
                        1 -> CodeInfoScreen(viewReviewUiState.submissionInfo)
                        2 -> ReviewCommentScreen(viewReviewViewModel)
                    }
                }
            }
        }
    }
}

/* 문제 내용 화면 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProblemInfoScreen(problemInfos: List<String>) {
    val keys = listOf("제목", "문제 설명", "입력 설명", "출력 설명", "시간 제한", "메모리 제한")

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

/* 작성한 코드 내용 화면 */
@Composable
fun CodeInfoScreen(submissionInfo: SubmissionDTO.GetSubmissionResponse) {
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

/* 리뷰 답글 목록 화면 */
@Composable
fun ReviewCommentScreen(viewReviewViewModel: ViewReviewViewModel) {
    val viewReviewUiState by viewReviewViewModel.viewReviewUiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AiReviewScreen(viewReviewUiState.aiReview)
//        AiReviewScreen(ReviewDTO.ReviewData(
//            0,
//            0,
//            "야채",
//            "완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다.",
//            listOf(
//                ReviewDTO.CommentData(0, 0, "불고기", "너무너무 잘 짠 코드라고 생각합니다.", "2025-11-11"),
//                ReviewDTO.CommentData(0, 0, "불고기", "너무너무 잘 짠 코드라고 생각합니다.", "2025-11-11"),
//                ReviewDTO.CommentData(0, 0, "불고기", "너무너무 잘 짠 코드라고 생각합니다.", "2025-11-11")),
//            "2025-10-31"
//        ))

        UserReviewScreen(viewReviewViewModel, viewReviewUiState.reviewComments)
//        UserReviewScreen(viewReviewViewModel,
//            listOf(
//                ReviewDTO.ReviewData(
//                    0,
//                    0,
//                    "야채",
//                    "완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다.",
//                    listOf(
//                        ReviewDTO.CommentData(0, 0, "불고기", "너무너무 잘 짠 코드라고 생각합니다.", "2025-11-11"),
//                        ReviewDTO.CommentData(0, 0, "불고기", "너무너무 잘 짠 코드라고 생각합니다.", "2025-11-11"),
//                        ReviewDTO.CommentData(0, 0, "불고기", "너무너무 잘 짠 코드라고 생각합니다.", "2025-11-11")),
//                    "2025-10-31"
//                ),
//                ReviewDTO.ReviewData(
//                    0,
//                    0,
//                    "야채",
//                    "완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다.",
//                    listOf(
//                        ReviewDTO.CommentData(0, 0, "불고기", "너무너무 잘 짠 코드라고 생각합니다.", "2025-11-11"),
//                        ReviewDTO.CommentData(0, 0, "불고기", "너무너무 잘 짠 코드라고 생각합니다.", "2025-11-11"),
//                    ),
//                    "2025-10-31"
//                ),
//                ReviewDTO.ReviewData(
//                    0,
//                    0,
//                    "야채",
//                    "완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다. 완벽한 코드입니다.",
//                    emptyList(),
//                    "2025-10-31"
//                )
//            )
//        )
    }
}

/* AI 리뷰 화면 */
@Composable
fun AiReviewScreen(reviewInfo: ReviewDTO.ReviewData) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

                Text(
                    text = "AI 코드 리뷰",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "AI 코드 리뷰 열림/닫힘 아이콘",
                    modifier = Modifier
                        .size(50.dp)
                        .rotate(rotation),
                    tint = PrimaryColor
                )
            }

            AnimatedVisibility(visible = expanded) {
                if (reviewInfo.equals(ReviewDTO.ReviewData())) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "AI 리뷰가 없어요",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else {
                    ViewAiReviewItem(reviewInfo)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

/* AI 리뷰 출력 아이템 */
@Composable
fun ViewAiReviewItem(reviewInfo: ReviewDTO.ReviewData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 0.5.dp, color = PrimaryColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = reviewInfo.content,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )
        }
    }
}

/* 사용자 리뷰 화면 */
@Composable
fun UserReviewScreen(viewReviewViewModel: ViewReviewViewModel, reviewComments: List<ReviewDTO.ReviewData>) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

                Text(
                    text = "회원들이 남긴 리뷰",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "사용자 리뷰 열림/닫힘 아이콘",
                    modifier = Modifier
                        .size(50.dp)
                        .rotate(rotation),
                    tint = PrimaryColor
                )
            }

            AnimatedVisibility(visible = expanded) {
                if (reviewComments.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "리뷰가 없어요",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium)),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(reviewComments) { review ->
                            ReviewItem(viewReviewViewModel, review)
                        }
                    }
                }
            }
        }
    }
}

/* 리뷰 출력 아이템 */
@Composable
fun ReviewItem(viewReviewViewModel: ViewReviewViewModel, reviewInfo: ReviewDTO.ReviewData) {
    val viewReviewUiState by viewReviewViewModel.viewReviewUiState.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 0.5.dp, color = PrimaryColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 리뷰 작성자 닉네임, 생성일
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reviewInfo.nickname,
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )

                Text(
                    text = reviewInfo.createdAt.replace("T", " "),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light)),
                    color = Color.Gray
                )
            }

            // 리뷰 내용
            Text(
                text = reviewInfo.content,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                color = PrimaryColor,
                thickness = 0.5.dp
            )

            // 답글 목록
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (reviewInfo.comments.isEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "답글이 없어요",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                        )
                    }
                }
                else {
                    reviewInfo.comments.forEach { comment ->
                        ContentItem(comment)
                    }
                }
            }

            // 답글 작성 필드, 등록 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(0.6f),
                    value = viewReviewUiState.comments[reviewInfo.reviewId] ?: "",
                    onValueChange = { viewReviewViewModel.updateComment(reviewInfo.reviewId, it) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        errorBorderColor = MaterialTheme.colorScheme.error
                    ),
                    placeholder = {
                        Text(
                            text = "답글을 작성해 보세요",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
                        )
                    }
                )
                
                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = { viewReviewViewModel.submitComment(reviewInfo.reviewId) },
                    modifier = Modifier
                        .weight(0.3f)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PointColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "등록",
                        fontSize = 16.sp,
                        color = PrimaryColor,
                        fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                    )
                }
            }
        }
    }
}

/* 답글 출력 아이템 */
@Composable
fun ContentItem(contentInfo: ReviewDTO.CommentData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.SubdirectoryArrowRight,
            contentDescription = "답글 아이콘",
            modifier = Modifier.size(14.dp),
            tint = PrimaryColor
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 답글 작성자 닉네임, 생성일
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = contentInfo.nickname,
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_medium))
                )

                Text(
                    text = contentInfo.createdAt.replace("T", " "),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.spoqahansansneo_light)),
                    color = Color.Gray
                )
            }

            // 답글 내용
            Text(
                text = contentInfo.content,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.spoqahansansneo_light))
            )
        }
    }
}