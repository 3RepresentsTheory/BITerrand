package com.example.biterrand_fix

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.biterrand_fix.ui.theme.BITerrand_fixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BITerrand_fixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    ErrandScreen()
                    ErrandApp()
                }
            }
        }
    }

    /**
     * clear the cache data produced in activity
     */
    override fun onStop() {
        clearCache(this)
        super.onStop()
    }
}

fun clearCache(context: Context){
    val cacheDir = context.cacheDir
    if(cacheDir.isDirectory){
        cacheDir.listFiles()?.forEach { file ->
            file.delete()
        }
    }
}
