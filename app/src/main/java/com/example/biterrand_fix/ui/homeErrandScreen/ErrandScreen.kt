package com.example.biterrand_fix.ui.homeErrandScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.biterrand_fix.ErrandApp
import com.example.biterrand_fix.ErrandBottonNavBar
import com.example.biterrand_fix.R
import com.example.biterrand_fix.data.DemandRepository
import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.data.FakeDemandSource
import com.example.biterrand_fix.ui.*
import com.example.biterrand_fix.ui.BasicNavigation.NavigationDestination
import com.example.biterrand_fix.ui.DemandPropose.DemandProposeDestination
import com.example.biterrand_fix.ui.theme.*


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
        when(viewModel.errandUiState){
            is ErrandUiState.Loading -> {
                Text(text = "isLoading/wait to implement")}
            is ErrandUiState.Error   -> {
                Text(text = "isError/wait to implement with ${(viewModel.errandUiState as ErrandUiState.Error).exceptionDescription}")}
            is ErrandUiState.Success -> {
                ErrandScreenList(
                    viewModel = viewModel,
                    errandUiState = viewModel.errandUiState,
                    modifier = Modifier.padding(top = 100.dp)
                )
            }
            else ->{
                Text(text = "impossible")}
        }

    }
}