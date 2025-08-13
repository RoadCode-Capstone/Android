package com.example.roadcode.view.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.School
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.roadcode.ui.theme.PointColor
import com.example.roadcode.ui.theme.PrimaryColor

/* 하단 내비게이션 바 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavItem("roadmap_list", Icons.Outlined.School),
        NavItem("home", Icons.Outlined.Home),
        NavItem("ranking", Icons.Outlined.EmojiEvents)
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route   // 현재 화면

    BottomNavigation(
        backgroundColor = PrimaryColor
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.route,
                        tint = if (currentRoute == item.route) PointColor else Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                       },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(item.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
            )
        }
    }

}

/* 하단 내비게이션 바 아이템 */
data class NavItem(
    val route: String,
    val icon: ImageVector
)