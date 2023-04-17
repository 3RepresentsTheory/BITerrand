package com.example.biterrand_fix.ui.homeErrandScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.biterrand_fix.R
import com.example.biterrand_fix.data.FakeDemandSource
import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.UserBasicInfo
import com.example.biterrand_fix.ui.*
import com.example.biterrand_fix.ui.theme.Purple40
import com.example.biterrand_fix.ui.theme.PurpleGrey40
import com.example.biterrand_fix.ui.theme.PurpleGrey80
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun ErrandScreenList(
    modifier: Modifier = Modifier,
    viewModel: ErrandScreenViewModel,
    errandUiState: ErrandUiState
){
    val isLoading     by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    /**
     * I don't know what this 's used for , it can
     * use isLoading directly I think
     */
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = viewModel::getNewestDemandList
    ) {
        when(errandUiState){
            is ErrandUiState.Success->{
                BoxWithConstraints {
                    val height = maxHeight-with(LocalDensity.current){80.dp}
                    val listState = rememberLazyListState()
                    /*buffer is a indicator when there is buffer number of item to the end,
                    * it will trigger then onLoadMore*/
                    val buffer = 1
                    val loadMore = remember{
                        derivedStateOf {
                            val layoutInfo = listState.layoutInfo
                            val totalItemNumber = layoutInfo.totalItemsCount
                            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index?:0)+1
                            lastVisibleItemIndex > (totalItemNumber-buffer)
                        }
                    }

                    LazyColumn(
                        modifier=Modifier.height(height),
                        state = listState,
                        content = {
                            item {
                                Column{
                                    Text(text = "Pseudo block")
                                    Text(text = "Pseudo block")
                                    Text(text = "Pseudo block")
                                }
                            }
                            items(errandUiState.demands){ demand->
                                var userBasicInfoState  by remember {
                                    mutableStateOf(UserBasicInfo(
                                        userId  =0,
                                        userName= "...",
                                        avatarUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"
                                    ))
                                }
                                LaunchedEffect(demand.requirementProposer){
                                    userBasicInfoState=viewModel.getUserNameAvatar(demand.requirementProposer)
                                }
                                ErrandDemandCard(
                                    demandInfo = demand,
                                    userBasicInfo = userBasicInfoState
                                )
                            }
                            if(isLoadingMore){
                                item{Text(text = "你刷的太快了")}
                            }
                        },
                    )
                    LaunchedEffect(loadMore){
                        snapshotFlow {loadMore.value}
                            .filter { it }
                            .collect{
                                viewModel.loadingNewDemand()
                            }
                    }
                }
            }
            else ->{
                Text(text = "not implement yet,this should be handled" +
                        "when draw down cannot load data")
            }
        }
    }
}

@Composable
fun ErrandDemandCard(
    demandInfo: Demand,
    modifier: Modifier= Modifier,
    userBasicInfo: UserBasicInfo
){
    Card(
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
            .wrapContentHeight()
//            .height(250.dp)
            .fillMaxWidth(),
    ) {

        Column (modifier = Modifier.padding(8.dp)){
            UserAddressState(demandInfo = demandInfo,userBasicInfo= userBasicInfo)
            ContentBlock    (demandInfo = demandInfo)
            PriceAndDDL     (demandInfo = demandInfo)
        }
    }
}

@Composable
fun DemandNameAndFromTo(
    modifier: Modifier=Modifier,
    demandInfo: Demand,
    userName:String
){
    Column (
        modifier = modifier
            .padding(start = 8.dp)
    ){
        Row{
            Text(
//                text = demandInfo.requirementProposer,
                text = userName,
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = UserFriendlyTime(demandInfo.timestamp),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                modifier = Modifier.padding(4.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.width(240.dp)
            ){
                Text(
                    text = "From:"+demandInfo.startPlace,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "To    :"+demandInfo.finalPlace,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            //The status icon
            if (demandInfo.requirementSupplier!=null){
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Some one has supplied for this",
                    modifier = Modifier.padding(end = 24.dp, top = 4.dp),
                )
                //no supplier, check whether overdue
            }else if(isOverDue(demandInfo.timestamp)){
//                ImageVector(R.drawable.timelapse_fill0_wght400_grad0_opsz48)
                Image(
                    painter = painterResource(id = R.drawable.event_busy_fill0_wght400_grad0_opsz48) ,
                    contentDescription = "the demand is overdue",
                    modifier = Modifier
                        .padding(end = 24.dp, top = 4.dp)
                        .width(24.dp)
                        .aspectRatio(1f)
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.radio_button_unchecked_fill0_wght400_grad0_opsz48) ,
                    contentDescription = "the demand is overdue",
                    modifier = Modifier
                        .padding(end = 24.dp, top = 4.dp)
                        .width(24.dp)
                        .aspectRatio(1f)
                )

            }
        }
    }
}

@Composable
fun UserAddressState(
    modifier :Modifier= Modifier,
    demandInfo: Demand,
    userBasicInfo: UserBasicInfo
){

    Row (
        modifier = Modifier
            .wrapContentHeight()
    ){
        //the user Avatar
        AsyncImage(
            modifier= Modifier
                .height(40.dp)
                .width(40.dp)
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(userBasicInfo.avatarUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
//                    placeholder =
//                    error =
        )
        //rest info
        DemandNameAndFromTo(
            demandInfo = demandInfo,
            userName = userBasicInfo.userName
        )
    }
}

@Composable
fun ContentBlock(
    modifier:Modifier= Modifier,
    demandInfo: Demand
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .height(64.dp)
//            .wrapContentHeight()
    ){
        Text(
            text = demandInfo.orderDescription,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(7f)
                .padding(8.dp),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
//                Spacer(modifier = Modifier.weight(3f))
        if(demandInfo.imageUrl!=null){
            AsyncImage(
                modifier= Modifier
                    .aspectRatio(1f)
                    .weight(2f)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraSmall),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(demandInfo.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
//                    placeholder =
//                    error =
            )
        }
    }
}

@Composable
fun PriceAndDDL(
    modifier:Modifier= Modifier,
    demandInfo: Demand
){
    Row (
        modifier = Modifier
            .padding(start = 8.dp)
    ){
        Row(
            modifier = Modifier.clip(MaterialTheme.shapes.extraSmall)
        ){
            Text(
                text = "￥",
                modifier = Modifier
                    .background(PurpleGrey40)
                ,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "${demandInfo.price}",
                modifier = Modifier
                    .background(PurpleGrey40)
                    .width(36.dp)
                ,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            modifier = Modifier.clip(MaterialTheme.shapes.extraSmall)
        ){
            Text(
                text = "DDL",
                modifier = Modifier
                    .width(48.dp)
                    .background(PurpleGrey40),
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            Text(
                text = DDLTime(demandInfo.timeout),
                modifier = Modifier
                    .background(PurpleGrey80)
                    .wrapContentWidth()
                    .padding(start = 8.dp, end = 8.dp),
                color = if(isOverDue(demandInfo.timeout)) PurpleGrey40 else Purple40,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


//@Preview
//@Composable
//private fun ErrandDemandCardPreview(){
//    ErrandDemandCard(demandInfo = FakeDemandSource.demandList[0])
//}
