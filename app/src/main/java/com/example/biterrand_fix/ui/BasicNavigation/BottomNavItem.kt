package com.example.biterrand_fix.ui.BasicNavigation

import android.content.Intent.ShortcutIconResource
import com.example.biterrand_fix.R
import com.example.biterrand_fix.ui.SettingPage.SettingDestination
import com.example.biterrand_fix.ui.homeErrandScreen.HomeDestination

sealed class BottomNavItem (
    var route:String,
    var title:String,
//    temporary disable the icon on the buttom
    var iconResouce:Int,
){
    object ErrandHome : BottomNavItem(
        HomeDestination.route,
        HomeDestination.title,
        R.drawable.radio_button_unchecked_fill0_wght400_grad0_opsz48
    )
    object SettingPage: BottomNavItem(
        SettingDestination.route,
        SettingDestination.title,
        R.drawable.event_busy_fill0_wght400_grad0_opsz48
    )
}