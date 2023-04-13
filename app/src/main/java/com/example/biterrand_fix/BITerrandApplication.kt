package com.example.biterrand_fix

import android.util.Log
import android.app.Application
import com.example.biterrand_fix.data.AppContainer
import com.example.biterrand_fix.data.DefaultAppContainer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BITerrandApplication :Application(){
    lateinit var container: AppContainer
    lateinit var currentDate :LocalDate
    lateinit var dateFormatter:DateTimeFormatter

    override fun onCreate() {
        super.onCreate()
        Log.d("TDEBUG","Application() is created!")
        container   = DefaultAppContainer()
        currentDate = LocalDate.now()
        dateFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }
}