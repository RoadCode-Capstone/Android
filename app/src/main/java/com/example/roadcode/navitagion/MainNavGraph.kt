package com.example.roadcode.navitagion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roadcode.view.HomeScreen
import com.example.roadcode.view.LevelTestReadyScreen
import com.example.roadcode.view.LevelTestResultScreen
import com.example.roadcode.view.LevelTestScreen
import com.example.roadcode.view.MypageScreen
import com.example.roadcode.view.PasswordScreen
import com.example.roadcode.view.PointScreen
import com.example.roadcode.view.RankingScreen
import com.example.roadcode.view.RoadmapListScreen
import com.example.roadcode.view.RoadmapPlanAlgorithmScreen
import com.example.roadcode.view.RoadmapPlanGoalScreen
import com.example.roadcode.view.RoadmapPlanLanguageScreen
import com.example.roadcode.view.RoadmapPlanTypeScreen
import com.example.roadcode.view.RoadmapScreen
import com.example.roadcode.view.UserInfoScreen
import com.example.roadcode.viewmodel.AttendanceViewModel
import com.example.roadcode.viewmodel.CalendarViewModel
import com.example.roadcode.viewmodel.LevelTestViewModel
import com.example.roadcode.viewmodel.PasswordViewModel
import com.example.roadcode.viewmodel.PointViewModel
import com.example.roadcode.viewmodel.RankingViewModel
import com.example.roadcode.viewmodel.RoadmapPlanViewModel
import com.example.roadcode.viewmodel.RoadmapViewModel
import com.example.roadcode.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    val attendanceViewModel: AttendanceViewModel = hiltViewModel()  // 앱 실행 시 출석 체크 자동 실행
    val roadmapPlanViewModel: RoadmapPlanViewModel = hiltViewModel()
    val levelTestViewModel: LevelTestViewModel = hiltViewModel()
    val roadmapViewModel: RoadmapViewModel = hiltViewModel()
    val rankingViewModel: RankingViewModel = hiltViewModel()
    val calendarViewModel: CalendarViewModel = hiltViewModel()
    val pointViewModel: PointViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("plan_language") { RoadmapPlanLanguageScreen(navController, roadmapPlanViewModel) }                                  // 학습 계획 설정 화면 (언어)
        composable("plan_type") { RoadmapPlanTypeScreen(navController, roadmapPlanViewModel) }                                          // 학습 계획 설정 화면 (유형)
        composable("plan_algorithm") { RoadmapPlanAlgorithmScreen(navController, roadmapPlanViewModel) }                                // 학습 계획 설정 화면 (알고리즘)
        composable("plan_goal") { RoadmapPlanGoalScreen(navController, roadmapPlanViewModel) }                                          // 학습 계획 설정 화면 (일일 학습 목표)
        composable("level_ready") { LevelTestReadyScreen(navController, roadmapPlanViewModel, levelTestViewModel) }                     // 레벨 테스트 준비 화면
        composable("level_test") { LevelTestScreen(navController, roadmapPlanViewModel, levelTestViewModel) }                           // 레벨 테스트 화면
        composable("level_result") { LevelTestResultScreen(navController, roadmapPlanViewModel, levelTestViewModel, roadmapViewModel) } // 레벨 테스트 결과 화면
        composable("roadmap") { RoadmapScreen(navController, roadmapViewModel) }                                                        // 로드맵 조회 화면
        composable("mypage") { MypageScreen(navController) }                                                                            // 마이페이지 화면
        composable("point") { PointScreen(navController, pointViewModel) }                                                              // 포인트 내역 조회 화면
        composable("userInfo") { UserInfoScreen(navController, userViewModel) }                                                         // 회원 정보 조회 화면
        composable("password") { backStackEntry ->                                                                // 비밀번호 변경 화면
            val passwordViewModel: PasswordViewModel = hiltViewModel(backStackEntry)
            PasswordScreen(navController, passwordViewModel)
        }

        bottomNavGraph(navController, roadmapViewModel, calendarViewModel, rankingViewModel) // 하단 내비게이션 바
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.bottomNavGraph(navController: NavHostController, roadmapViewModel: RoadmapViewModel, calendarViewModel: CalendarViewModel, rankingViewModel: RankingViewModel) {
    composable("roadmap_list") { RoadmapListScreen(navController, roadmapViewModel) }   // 로드맵 목록 화면
    composable("home") { HomeScreen(navController, calendarViewModel) }                 // 홈 화면
    composable("ranking") { RankingScreen(navController, rankingViewModel) }            // 순위 화면
}