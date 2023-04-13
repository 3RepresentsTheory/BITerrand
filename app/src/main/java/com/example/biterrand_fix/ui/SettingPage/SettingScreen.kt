package com.example.biterrand_fix.ui.SettingPage

import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.biterrand_fix.ui.BasicNavigation.NavigationDestination


object SettingDestination: NavigationDestination {
    override val route = "setting"
    override val title = "设置"
}

@Composable
fun SettingScreen (
    modifier: Modifier = Modifier,
    bottomMenu: @Composable ()->Unit,
){
    Scaffold(
        bottomBar = bottomMenu
    ) {scaffoldPadding->
        Text(text = "this is setting screen")
    }
}