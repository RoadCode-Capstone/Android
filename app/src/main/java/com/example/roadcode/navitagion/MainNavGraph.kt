package com.example.roadcode.navitagion

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

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    val roadmapPlanViewModel: RoadmapPlanViewModel = hiltViewModel()
    val levelTestViewModel: LevelTestViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "plan_language") {
        composable("plan_language") { RoadmapPlanLanguageScreen(navController, roadmapPlanViewModel) }
        composable("plan_type") { RoadmapPlanTypeScreen(navController, roadmapPlanViewModel) }
        composable("plan_algorithm") { RoadmapPlanAlgorithmScreen(navController, roadmapPlanViewModel) }
        composable("plan_goal") { RoadmapPlanGoalScreen(navController, roadmapPlanViewModel) }
        composable("level_ready") { LevelTestReadyScreen(navController, roadmapPlanViewModel, levelTestViewModel) }
        composable("level_test") { LevelTestScreen(navController, levelTestViewModel) }
        composable("level_result") { LevelTestResultScreen(navController) }
    }
}