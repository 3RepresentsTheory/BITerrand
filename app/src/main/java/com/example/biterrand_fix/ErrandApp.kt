package com.example.biterrand_fix

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.biterrand_fix.ui.BasicNavigation.BottomNavItem
import com.example.biterrand_fix.ui.BasicNavigation.ErrandNavHost


@Composable
fun ErrandApp(navController: NavHostController = rememberNavController()){
    ErrandNavHost(navController= navController)
}


@Composable
fun ErrandTopAppBar(

){

}

@Composable
fun ErrandBottonNavBar(
    navController: NavHostController
){
    val bottomItems = listOf(
        BottomNavItem.ErrandHome,
        BottomNavItem.SettingPage
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        bottomItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.iconResouce), contentDescription = item.title) },
//                label = { Text(text = item.title,
//                    fontSize = 9.sp) },
//                selectedContentColor = Color.Black,
//                unselectedContentColor = Color.Black.copy(0.4f),
                selected = (currentRoute==item.route),
                onClick = {
                    navController.navigate(item.route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
//    BottomNavigation {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
//        bottomItems.forEach{item ->
//            BottomNavigationItem(
//                icon = { Icon(painterResource(id = item.iconResouce), contentDescription = item.title) },
////                label = { Text(text = item.title,
////                    fontSize = 9.sp) },
////                selectedContentColor = Color.Black,
////                unselectedContentColor = Color.Black.copy(0.4f),
//
//                alwaysShowLabel = true,
//                selected = (currentRoute==item.route),
//                onClick = {
//                    navController.navigate(item.route) {
//
//                        navController.graph.startDestinationRoute?.let { screen_route ->
//                            popUpTo(screen_route) {
//                                saveState = true
//                            }
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }
//    }
}