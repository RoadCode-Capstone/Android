package com.example.roadcode.navitagion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roadcode.view.LevelTestReadyScreen
import com.example.roadcode.view.LevelTestResultScreen
import com.example.roadcode.view.LevelTestScreen
import com.example.roadcode.view.RoadmapPlanAlgorithmScreen
import com.example.roadcode.view.RoadmapPlanGoalScreen
import com.example.roadcode.view.RoadmapPlanLanguageScreen
import com.example.roadcode.view.RoadmapPlanTypeScreen
import com.example.roadcode.viewmodel.LevelTestViewModel
import com.example.roadcode.viewmodel.RoadmapPlanViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    val roadmapPlanViewModel: RoadmapPlanViewModel = hiltViewModel()
    val levelTestViewModel: LevelTestViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "plan_language") {
        composable("plan_language") { RoadmapPlanLanguageScreen(navController, roadmapPlanViewModel) }              // 학습 계획 설정 화면 (언어)
        composable("plan_type") { RoadmapPlanTypeScreen(navController, roadmapPlanViewModel) }                      // 학습 계획 설정 화면 (유형)
        composable("plan_algorithm") { RoadmapPlanAlgorithmScreen(navController, roadmapPlanViewModel) }            // 학습 계획 설정 화면 (알고리즘)
        composable("plan_goal") { RoadmapPlanGoalScreen(navController, roadmapPlanViewModel) }                      // 학습 계획 설정 화면 (일일 학습 목표)
        composable("level_ready") { LevelTestReadyScreen(navController, roadmapPlanViewModel, levelTestViewModel) } // 레벨 테스트 준비 화면
        composable("level_test") { LevelTestScreen(navController, roadmapPlanViewModel, levelTestViewModel) }       // 레벨 테스트 화면
        composable("level_result") { LevelTestResultScreen(navController, levelTestViewModel) }                     // 레벨 테스트 결과 화면
    }
}