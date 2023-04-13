package com.example.biterrand_fix.ui.DemandGet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.biterrand_fix.ui.BasicNavigation.NavigationDestination

object DemandGetDestination:NavigationDestination{
    override val route: String = "demandget"
    override val title: String = "我来接单"

    const val demandIdArg      = "demandId"
    val routeWithArgs          = "$route/{$demandIdArg}"
}

@Composable
fun DemandGetScreen(
    modifier: Modifier = Modifier,
    navigateBack:()->Unit
){

}