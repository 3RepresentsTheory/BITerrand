package com.example.biterrand_fix.ui.BasicNavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.biterrand_fix.ErrandBottonNavBar
import com.example.biterrand_fix.ui.DemandGet.DemandGetDestination
import com.example.biterrand_fix.ui.DemandGet.DemandGetScreen
import com.example.biterrand_fix.ui.DemandPropose.DemandProposeDestination
import com.example.biterrand_fix.ui.DemandPropose.DemandProposeScreen
import com.example.biterrand_fix.ui.SettingPage.SettingDestination
import com.example.biterrand_fix.ui.SettingPage.SettingScreen
import com.example.biterrand_fix.ui.homeErrandScreen.ErrandScreen
import com.example.biterrand_fix.ui.homeErrandScreen.HomeDestination


@Composable
fun ErrandNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
){
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier,
    ){
        composable(HomeDestination.route){
            ErrandScreen(
                navigateToDemandEntry   =
                    {navController.navigate("${DemandGetDestination.routeWithArgs}/${it}")},
                navigateToProposeDemand =
                    {navController.navigate(DemandProposeDestination.route)},
                bottomMenu = {ErrandBottonNavBar(navController = navController)},
            )
        }
        composable(SettingDestination.route){
            SettingScreen(
                bottomMenu = {ErrandBottonNavBar(navController = navController)},
            )
        }
        composable(DemandProposeDestination.route){
            DemandProposeScreen(
                navigateBack = {navController.navigateUp()}
            )
        }
        composable(
            route = DemandGetDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DemandGetDestination.demandIdArg){
                type = NavType.LongType
                }
            )
        )
        {
            DemandGetScreen(
                navigateBack = {navController.navigateUp()}
            )
        }
    }
}
