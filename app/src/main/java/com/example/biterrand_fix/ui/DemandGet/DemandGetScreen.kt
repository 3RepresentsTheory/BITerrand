package com.example.biterrand_fix.ui.DemandGet

import com.example.biterrand_fix.R
import android.telephony.mbms.MbmsDownloadReceiver
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.UserBasicInfo
import com.example.biterrand_fix.ui.AppViewModelProvider
import com.example.biterrand_fix.ui.BasicNavigation.NavigationDestination
import com.example.biterrand_fix.ui.ErrandScreenViewModel
import com.example.biterrand_fix.ui.UserFriendlyTime
import com.example.biterrand_fix.ui.theme.DeepBlue

object DemandGetDestination : NavigationDestination {
    override val route: String = "demandget"
    override val title: String = "我来接单"

    const val demandIdArg = "demandId"
    val routeWithArgs = "$route/{$demandIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemandGetScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: DemandGetScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Scaffold(
        topBar = {
            Text(text = "in detail")
        },
        bottomBar = {
            Text(text = "buttom bar wait to implement")
        }
    ) { innerpadding ->
        val demandGetUiState = viewModel.demandGetUiState
        when (demandGetUiState) {
            is AggregateInfoUiState.Loading -> {
                Text(text = "loading user info")
            }
            is AggregateInfoUiState.Error -> {
                Text(text = "info error")
            }
            is AggregateInfoUiState.Success -> {
                val demand = demandGetUiState.info.demand
                val userinfo = demandGetUiState.info.userinfo
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerpadding)
                        .padding(36.dp, 8.dp)

                ) {
                    UserAndTime(
                        demandInfo = demand,
                        userInfo = userinfo,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    PriceShow(price = demand.price)
                    Spacer(modifier = Modifier.padding(8.dp))
                    AddressAndDDL(
                        addressFrom = demand.startPlace,
                        addressTo = demand.finalPlace,
                        ddlTime = demand.timeout
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    SpecificDemandAndAppendPhoto(
                        description = demand.orderDescription,
                        imageUrl = demand.imageUrl,
                    )
//                    Text(text = "${(viewModel.demandGetUiState as AggregateInfoUiState.Success).info.demand}")
//                    Text(text = "${(viewModel.demandGetUiState as AggregateInfoUiState.Success).info.userinfo}")
                }
            }
        }
    }
}

@Composable
fun UserAndTime(
    modifier: Modifier = Modifier,
    demandInfo: Demand,
    userInfo: UserBasicInfo,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),

        ) {
        AsyncImage(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(50.dp)
                .aspectRatio(1f)
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(userInfo.avatarUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 8.dp)
        ) {
            Text(
                text = userInfo.userName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Divider(
                color = DeepBlue
            )
            Text(
                text = "发布于:${UserFriendlyTime(demandInfo.timestamp)}",
                style = MaterialTheme.typography.bodyMedium

            )
        }
        Spacer(modifier = Modifier.padding(32.dp))
    }
}

@Composable
fun PriceShow(
    modifier: Modifier = Modifier,
    price: Long
) {
    Row(
        modifier = modifier
//            .clip(MaterialTheme.shapes.extraSmall)
            .background(DeepBlue)
            .padding(4.dp)
    ) {
        Text(
            text = "目标价 : ${price} ￥",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun AddressAndDDL(
    modifier: Modifier = Modifier,
    addressFrom: String,
    addressTo: String,
    ddlTime: String,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
        ) {
            Image(
                painterResource(id = R.drawable.starting),
                "starting position",
                modifier = Modifier
                    .height(24.dp)
                    .aspectRatio(1f)
            )
            Text(
                text = addressFrom
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier
                .wrapContentHeight()
        ) {
            Image(
                painterResource(id = R.drawable.destination),
                "destination",
                modifier = Modifier
                    .height(24.dp)
                    .aspectRatio(1f)
            )
            Text(
                text = addressTo
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier
                .wrapContentHeight()
        ) {
            Image(
                painterResource(id = R.drawable.destination),
                "DDL",
                modifier = Modifier
                    .height(24.dp)
                    .aspectRatio(1f)
            )
            Text(
                text = UserFriendlyTime(ddlTime)
            )
        }
    }
}

@Composable
fun SpecificDemandAndAppendPhoto(
    modifier: Modifier = Modifier,
    description:String,
    imageUrl   :String?,
) {
    LazyColumn(
        modifier =modifier.fillMaxWidth()
    ) {
        item{
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        if(imageUrl!=null) {
            item {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "this is appended photo",
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}
