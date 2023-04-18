package com.example.biterrand_fix.ui.homeErrandScreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biterrand_fix.ui.*
import com.example.biterrand_fix.ui.BasicNavigation.NavigationDestination


object HomeDestination:NavigationDestination{
    override val route = "home"
    override val title = "来跑腿"
}


@Composable
fun ErrandScreen (
    navigateToDemandEntry:()->Unit,
    navigateToProposeDemand:(Int)->Unit,
    bottomMenu:@Composable ()->Unit,
    modifier: Modifier = Modifier,
    viewModel: ErrandScreenViewModel = viewModel(factory = ErrandScreenViewModel.Factory),
){
//    val mockList = listOf(
//        FakeDemandSource.demandList[0],
//        FakeDemandSource.demandList[1],
//        FakeDemandSource.demandList[0],
//        FakeDemandSource.demandList[1],
//        FakeDemandSource.demandList[0],
//        FakeDemandSource.demandList[1],
//        FakeDemandSource.demandList[0],
//        FakeDemandSource.demandList[1],
//        FakeDemandSource.demandList[0],
//        FakeDemandSource.demandList[1],
//        FakeDemandSource.demandList[0],
//        FakeDemandSource.demandList[1],
//    )
    Log.d("TEMPDEBUG","ErrandScreen is composing")

    Scaffold (
        topBar =
        {
                Text("just for test")
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "post demand", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
//        floatingActionButtonPosition = centerDocked
        bottomBar = bottomMenu
    ){  innerPadding->
        when(viewModel.errandUiInitialState){
            is ErrandUiInitialState.Loading -> {
                Text(text = "isLoading/wait to implement")}
            is ErrandUiInitialState.Error   -> {
                Text(text = "isError/wait to implement with ${(viewModel.errandUiInitialState as ErrandUiInitialState.Error).exceptionDescription}")}
            is ErrandUiInitialState.Success -> {
                ErrandScreenList(
                    viewModel = viewModel,
                    modifier = Modifier.padding(top = 100.dp)
                )
            }
            else ->{
                Text(text = "impossible")}
        }

    }
}