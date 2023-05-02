package com.example.biterrand_fix.ui.homeErrandScreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biterrand_fix.ui.*
import com.example.biterrand_fix.ui.BasicNavigation.NavigationDestination


object HomeDestination:NavigationDestination{
    override val route = "home"
    override val title = "来跑腿"
}

val errandScreenDebug = "errandScreenDebug"

@Composable
fun ErrandScreen (
    navigateToDemandEntry:(Long)->Unit,
    navigateToProposeDemand:()->Unit,
    bottomMenu:@Composable ()->Unit,
    modifier: Modifier = Modifier,
    viewModel: ErrandScreenViewModel = viewModel(factory = ErrandScreenViewModel.Factory),
){
    Log.d(errandScreenDebug,"errand screeen compose")
    Scaffold (
        topBar =
        {
                Text("just for test")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToProposeDemand()
                }
            )
            {
                Icon(imageVector = Icons.Default.Add, contentDescription = "post demand", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        bottomBar = bottomMenu
    ){  innerPadding->
        //initial loading page
        when(viewModel.errandUiInitialState){
            is ErrandUiInitialState.Loading -> {
                Text(text = "isLoading/wait to implement")
            }
            is ErrandUiInitialState.Error   -> {
                Column(
                    modifier=Modifier.fillMaxSize()
                ) {
                    Text(text = "isError/wait to implement with ${(viewModel.errandUiInitialState as ErrandUiInitialState.Error).exceptionDescription}")
                    Button(
                        onClick = {viewModel.getInitNewestDemandList()}
                    ){
                        Text(text = "点击重试")
                    }
                }
            }
            is ErrandUiInitialState.Success -> {
                ErrandScreenList(
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding),
                    navigateToDemandEntry = navigateToDemandEntry
                )
            }
            else ->{
                Text(text = "impossible")}
        }

    }
}