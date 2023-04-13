package com.example.biterrand_fix.ui.BasicNavigation


interface NavigationDestination {
    /**
     * unique name for the path
     */
    val route:String

    /**
     * the title of this path
     */
    val title:String
}