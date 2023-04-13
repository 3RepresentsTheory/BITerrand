package com.example.biterrand_fix

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.biterrand_fix.ui.ErrandScreenViewModel
import com.example.biterrand_fix.ui.theme.BITerrand_fixTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biterrand_fix.data.FakeDemandSource
import com.example.biterrand_fix.ui.ErrandUiState
import com.example.biterrand_fix.ui.homeErrandScreen.ErrandDemandCard
import com.example.biterrand_fix.ui.homeErrandScreen.ErrandScreen
import com.example.biterrand_fix.ui.theme.YellowGrey

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
                    ErrandScreen()

//                    Log.d("TDEBUG","applicationContext:${applicationContext}")
//
//                    val status :ErrandUiState= ErrandScreenViewModel.errandUiState
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                    ) {
//                        Button(onClick = { /*TODO*/ }, modifier = Modifier.height(32.dp)) {
//                            Text(text = "pseudo")
//                        }
//                        when(status){
//                            is ErrandUiState.Success -> Text(text = status.statusString)
//                            is ErrandUiState.Loading -> Text(text = "loading")
//                            is ErrandUiState.Error   -> Text(text = "error: ${status.exceptionDescription}")
//                            else -> {Text(text = "unknown")}
//                        }
//                    }

//                    LazyColumn(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentPadding = PaddingValues(16.dp)
//                    ){
//                        items(){demand->
//                            Text(text = demand.toString())
//                        }
//                    }
                }
            }
        }
    }
}