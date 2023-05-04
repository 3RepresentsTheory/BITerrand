package com.example.biterrand_fix

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.example.biterrand_fix.data.AppContainer
import com.example.biterrand_fix.data.DefaultAppContainer
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class BITerrandApplication :Application(){
    lateinit var container: AppContainer
    lateinit var currentDate :LocalDate
    lateinit var dateFormatter:DateTimeFormatter

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("TDEBUG","Application() is created!")
        container   = DefaultAppContainer()
        currentDate = LocalDate.now()
        dateFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        context     = applicationContext
    }
}
fun getAppContext(): Context {
    return BITerrandApplication.context
}
