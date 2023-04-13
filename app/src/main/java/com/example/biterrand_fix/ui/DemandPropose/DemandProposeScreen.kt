package com.example.biterrand_fix.ui.DemandPropose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.biterrand_fix.ui.BasicNavigation.NavigationDestination

object DemandProposeDestination :NavigationDestination{
    override val route: String = "demandpropose"
    override val title: String = "我来叫单"
}

@Composable
fun DemandProposeScreen(
    modifier: Modifier = Modifier,
    navigateBack:()->Unit,
){

}