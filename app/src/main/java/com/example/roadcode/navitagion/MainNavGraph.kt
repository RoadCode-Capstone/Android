package com.example.roadcode.navitagion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roadcode.view.RoadmapPlanAlgorithmScreen
import com.example.roadcode.view.RoadmapPlanGoalScreen
import com.example.roadcode.view.RoadmapPlanLanguageScreen
import com.example.roadcode.view.RoadmapPlanTypeScreen

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "plan_language") {
        composable("plan_language") { RoadmapPlanLanguageScreen(navController) }
        composable("plan_type") { RoadmapPlanTypeScreen(navController) }
        composable("plan_algorithm") { RoadmapPlanAlgorithmScreen(navController) }
        composable("plan_goal") { RoadmapPlanGoalScreen(navController) }
    }
}