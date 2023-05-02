package com.example.biterrand_fix.ui.DemandPropose

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.ui.AppViewModelProvider
import com.example.biterrand_fix.ui.BasicNavigation.NavigationDestination
import com.example.biterrand_fix.ui.theme.BITerrand_fixTheme
import com.example.biterrand_fix.ui.theme.DeepBlue
import com.example.biterrand_fix.ui.theme.ShallowBlue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File




val proposeDebugTag:String = "proposeDebugTag"
object DemandProposeDestination : NavigationDestination {
    override val route: String = "demandpropose"
    override val title: String = "我来叫单"
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DemandProposeScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DemandProposeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    //coroutine use for post demand
    Log.d(proposeDebugTag,"proposeScreen compose")
    val coroutineScope = rememberCoroutineScope()
    BackHandler(viewModel.sheetState.isVisible) {
        coroutineScope.launch { viewModel.sheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = viewModel.sheetState,
        sheetContent = { BottomSheet(viewModel) },
        modifier = Modifier.fillMaxSize()
    ) {

        Scaffold(
            topBar = {
                Text(text = "Propose a demand")
            },
            bottomBar = {
                Button(onClick = {
                    coroutineScope.launch {
                        viewModel.proposeDemand()
                        navigateBack()
                    }
                }) {
                    Text("提交")
                }
            }
        ) { innerpadding ->
            DemandProposeBody(
                modifier = Modifier.padding(innerpadding),
                onValueChange = viewModel::updateUiState,
                proposeUiState = viewModel.proposeUiState,
                sheetState = viewModel.sheetState
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DemandProposeBody(
    modifier: Modifier = Modifier,
    onValueChange: (Demand) -> Unit,
    proposeUiState: proposeUiState,
    sheetState: ModalBottomSheetState
//    onSelectPhoto: () -> Unit,
//    onPhotoTake: () -> Unit
) {


    Log.d(proposeDebugTag,"proposebody compose")
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.padding(32.dp))
        DemandForm(
            proposeUiState = proposeUiState,
            onValueChange = onValueChange,
            onAddPhotoClick = {
                coroutineScope.launch {
                    if (sheetState.isVisible) sheetState.hide()
                    else sheetState.show()
                }
            }
        )
    }

}


@Composable
fun DemandForm(
    proposeUiState: proposeUiState,
    modifier: Modifier = Modifier,
    onValueChange: (Demand) -> Unit = {},
    onAddPhotoClick: () -> Unit = {},
) {
    Log.d(proposeDebugTag,"demand form compose")
    DescriptionInputBox(
        demand = proposeUiState.demandInfo,
        onValueChange = onValueChange
    )
    AddingPhotoBlock(
        modifier = Modifier.height(160.dp),
        onAddPhotoClick = onAddPhotoClick,
        photoUiState = proposeUiState.photoUiState
    )
}


/**
 * composable function which is
 * the modified textfield box for editing description
 */
@Composable
fun DescriptionInputBox(
    demand: Demand,
    onValueChange: (Demand) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState(0)
    val mCustomTextSelectionColors = TextSelectionColors(
        handleColor = DeepBlue,
        backgroundColor = ShallowBlue
    )
    SelectionContainer {
        CompositionLocalProvider(
            LocalTextSelectionColors provides mCustomTextSelectionColors
        ) {
            TextField(
                modifier = modifier
                    .fillMaxWidth()
                    .height(136.dp)
                    .verticalScroll(scrollState),
                value = demand.orderDescription,
                onValueChange = { onValueChange(demand.copy(orderDescription = it)) },
                placeholder = {
                    Text(
                        text = "大家需要知道详细的派送地址，个人联系方式，以及跑腿物品信息...",
                        color = Color.Gray,
                        fontSize = 16.sp,
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = DeepBlue
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                )
            )
            LaunchedEffect(scrollState.maxValue) {
                scrollState.scrollTo(scrollState.maxValue)
            }
        }
    }
}


/**
 * bottomSheet for selecting photo
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    viewModel: DemandProposeScreenViewModel
) {
//    var getphoto: Uri? = null

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){result->
        if(result.resultCode==Activity.RESULT_OK){
            Log.d(proposeDebugTag,"photo successfully take")
            viewModel.updateUiImageState()
        }else{
            Toast.makeText(context, "没有加载图片", Toast.LENGTH_SHORT).show()
        }
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){isGranted->
        if(isGranted){
            // 授权通过，重新启动拍照程序
            Log.d(proposeDebugTag,"start the camera")
            selectImageLauncher.launch(
                viewModel.createTempFileAndLoadIntent(context)
            )
            coroutineScope.launch {
                Log.d(proposeDebugTag,"Hide the sheet")
                if (viewModel.sheetState.isVisible) viewModel.sheetState.hide()
                else viewModel.sheetState.show()
            }
        }else{
            Toast.makeText(context, "没有授权，无法使用照相机", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Button(onClick = {
            //检查授权Permission check
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                // 请求相机权限
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            } else {
                // 启动拍照程序
                Log.d(proposeDebugTag,"start the camera")
                selectImageLauncher.launch(
                    viewModel.createTempFileAndLoadIntent(context)
                )
                coroutineScope.launch {
                    Log.d(proposeDebugTag,"Hide the sheet")
                    if (viewModel.sheetState.isVisible) viewModel.sheetState.hide()
                    else viewModel.sheetState.show()
                }
            }

        }
        ) {
            Text(text = "使用照相机拍摄")
        }
//        Spacer(modifier = Modifier.height(32.dp))
//        Button(onClick = {getphoto=onSelectPhoto()}) {
//            Text(text = "选择本机照片")
//        }
    }
}



@Composable
fun AddingPhotoBlock(
    modifier: Modifier = Modifier,
    onAddPhotoClick: () -> Unit,
    photoUiState: photoUiState,
) {
    val context = LocalContext.current
    Log.d(proposeDebugTag,"photo composing: in photoUistate:isphotoadd:${photoUiState.isPhotoAdded} " +
            "photoUrl:${photoUiState.addPhotoUrl}")
    AsyncImage(
        model = if (photoUiState.isPhotoAdded) photoUiState.addPhotoUrl else null,
        modifier = modifier
            .aspectRatio(1f)
            .height(32.dp)
            .clickable {
                /*TODO 这里点击的时候没做好，如果用户没把键盘放下，这个键盘会挡住底部*/
                onAddPhotoClick() },
        contentScale = ContentScale.Crop,
        contentDescription = "selected Image"
    )
}


@Preview
@Composable
fun DemandProposeBodyPreview() {
//    BITerrand_fixTheme {
//        DemandProposeBody(
//            modifier = Modifier.fillMaxSize()
//        )
//    }
}

