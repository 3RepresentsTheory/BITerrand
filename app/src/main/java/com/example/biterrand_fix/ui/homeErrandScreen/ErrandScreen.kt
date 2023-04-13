package com.example.biterrand_fix.ui.homeErrandScreen

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
import com.example.biterrand_fix.R
import com.example.biterrand_fix.data.DemandRepository
import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.data.FakeDemandSource
import com.example.biterrand_fix.ui.DDLTime
import com.example.biterrand_fix.ui.ErrandScreenViewModel
import com.example.biterrand_fix.ui.UserFriendlyTime
import com.example.biterrand_fix.ui.isOverDue
import com.example.biterrand_fix.ui.theme.*

@Composable
fun ErrandScreen (
    modifier: Modifier = Modifier,
    viewModel: ErrandScreenViewModel = viewModel(factory = ErrandScreenViewModel.Factory)
){
    val mockList = listOf(
        FakeDemandSource.demandList[0],
        FakeDemandSource.demandList[1],
        FakeDemandSource.demandList[0],
        FakeDemandSource.demandList[1],
        FakeDemandSource.demandList[0],
        FakeDemandSource.demandList[1],
        FakeDemandSource.demandList[0],
        FakeDemandSource.demandList[1],
        FakeDemandSource.demandList[0],
        FakeDemandSource.demandList[1],
        FakeDemandSource.demandList[0],
        FakeDemandSource.demandList[1],
    )
    Scaffold (
        topBar = {Text("just for test")},
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "post demand", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
//        floatingActionButtonPosition = centerDocked
    ){innerPadding->
        LazyColumn{
            items(mockList){item ->
                ErrandDemandCard(demandInfo = item)
            }
        }
    }
}

@Composable
fun ErrandDemandCard(demandInfo:Demand,modifier: Modifier= Modifier){
    Card(
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
            .wrapContentHeight()
//            .height(250.dp)
            .fillMaxWidth(),
    ) {
        Column (modifier = Modifier.padding(8.dp)){
            UserAddressState(demandInfo = demandInfo)
            ContentBlock    (demandInfo = demandInfo)
            PriceAndDDL     (demandInfo = demandInfo)
        }
    }
}

@Composable
fun DemandNameAndFromTo(
    modifier: Modifier=Modifier,
    demandInfo: Demand,
){
    Column (
        modifier = modifier
            .padding(start = 8.dp)
    ){
        Row{
            Text(
//                text = demandInfo.requirementProposer,
                text = "习近平",
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
    demandInfo: Demand
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
                .data(demandInfo.imageUrl)
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
//                    .height(104.dp)
            .wrapContentHeight()
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
                color = White,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "${demandInfo.price}",
                modifier = Modifier
                    .background(PurpleGrey40)
                    .width(36.dp)
                ,
                color = White,
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
                color = White,
                textAlign = TextAlign.Center,
            )
            Text(
                text = DDLTime(demandInfo.timestamp),
                modifier = Modifier
                    .background(PurpleGrey80)
                    .wrapContentWidth()
                    .padding(start = 8.dp, end = 8.dp),
                color = if(isOverDue(demandInfo.timestamp)) PurpleGrey40 else Purple40,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Preview
@Composable
private fun ErrandDemandCardPreview(){
    ErrandDemandCard(demandInfo = FakeDemandSource.demandList[0])
}