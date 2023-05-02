package com.example.biterrand_fix

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.biterrand_fix.ui.DemandPropose.proposeDebugTag
import com.example.biterrand_fix.ui.theme.BITerrand_fixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(proposeDebugTag,"activity onCreate")
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
        Log.d(proposeDebugTag,"activity onStop")
//        clearCache(this)
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(proposeDebugTag,"activity onDestroy")
        super.onDestroy()
    }

    override fun onPause() {
        Log.d(proposeDebugTag,"activity onPause")
        super.onPause()
    }

    override fun onRestart() {
        Log.d(proposeDebugTag,"activity onRestart")
        super.onRestart()
    }

    override fun onResume() {
        Log.d(proposeDebugTag,"activity onResume")
        super.onResume()
    }

    override fun onStart() {
        Log.d(proposeDebugTag,"activity onStart")
        super.onStart()
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
