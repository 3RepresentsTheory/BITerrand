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

object DemandProposeDestination :NavigationDestination{
    override val route: String = "demandpropose"
    override val title: String = "我来叫单"
}

@Composable
fun DemandProposeScreen(
    modifier: Modifier = Modifier,
    navigateBack:()->Unit,
    viewModel: DemandProposeScreenViewModel  = viewModel(factory=AppViewModelProvider.Factory)
){
    //coroutine use for post demand
    val coroutineScope = rememberCoroutineScope()

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
    ) {
        innerpadding->
        DemandProposeBody(
            modifier= Modifier.padding(innerpadding),
            onValueChange = viewModel::updateUiState,
            proposeUiState = viewModel.proposeUiState,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DemandProposeBody(
    modifier: Modifier = Modifier,
    onValueChange: (Demand) -> Unit,
    proposeUiState: proposeUiState,
//    onSelectPhoto: () -> Unit,
//    onPhotoTake: () -> Unit
){
    //添加图片的底部选项
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val coroutineScope = rememberCoroutineScope()
    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
//        sheetContent = { BottomSheet(onSelectPhoto,onPhotoTake) },
        sheetContent = { BottomSheet() },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.padding(32.dp))
            DemandForm(
                demand = proposeUiState.demandInfo,
                onValueChange = onValueChange
            )
        }
    }

}


@Composable
fun DemandForm(
    demand: Demand,
    modifier: Modifier =Modifier,
    onValueChange:(Demand)->Unit ={},
){
    DescriptionInputBox(
        demand = demand,
        onValueChange = onValueChange
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
){
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
                onValueChange = {onValueChange(demand.copy(orderDescription = it))},
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
@Composable
fun BottomSheet(
//    onSelectPhoto: ()->Unit,
//    onPhotoTake:   ()->Unit
){
    var getphoto:Uri? = null
    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Text(text = "选择本机照片")
//        Button(onClick = {getphoto=onSelectPhoto()}) {
//            Text(text = "选择本机照片")
//        }
//        Spacer(modifier = Modifier.height(32.dp))
//        Button(onClick = {getphoto=onPhotoTake()}) {
//            Text(text = "是用照相机拍摄",)
//        }
    }
}
@Composable
fun AddPhotoBlock(
    modifier: Modifier = Modifier,
    onPhotoTake: (Uri)->Unit
){
    var GetSuccess:Boolean by remember{
        mutableStateOf(false)
    }
    var GetGranted:Boolean by remember {
        mutableStateOf(false)
    }
    var photoUri: Uri? by remember {
        mutableStateOf(null)
    }

    val context = LocalContext.current

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode== Activity.RESULT_OK){
            GetSuccess = true
        }else{
            /*failed*/
            GetSuccess = false
            Toast.makeText(context,"没有加载图片", Toast.LENGTH_SHORT).show()
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        GetGranted = isGranted
        //code below only use for debug
        if (isGranted) {
            // 启动相机应用程序
            Log.d("TDEBUG","CAMERA GRANTED")
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val tempFile = File.createTempFile("tempPicture",".png",context.cacheDir)
            photoUri = FileProvider.getUriForFile(context,context.packageName+".fileprovider",tempFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            GetSuccess=false
            selectImageLauncher.launch(takePictureIntent)
        } else {
            // 处理未授予相机权限的情况
            Log.d("TDEBUG","CAMERA NOT GRANTED")
            Toast.makeText(context,"没有授权，无法使用照相机", Toast.LENGTH_SHORT).show()
        }
    }

    Column{
        Button(
            onClick = {
                //检查授权Permission check
                if(ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                ){
                    // 请求相机权限
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }else{
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val tempFile = File.createTempFile("tempPicture",".png",context.cacheDir)
                    photoUri = FileProvider.getUriForFile(context,context.packageName+".fileprovider",tempFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    GetSuccess=false
                    selectImageLauncher.launch(takePictureIntent)
                }
            }
        ) {
            Text("点击进行拍照")
        }

        if(GetSuccess){
            //Use Coil to display the selected image
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(data = photoUri)
                    .build()
            )

//            Image(
//                painter = painter,
//                contentDescription = null,
//                modifier = Modifier
//                    .padding(5.dp)
//                    .fillMaxWidth()
//                    .clickable {
//                        coroutineScope.launch {
//                            if (sheetState.isVisible) sheetState.hide()
//                            else sheetState.show()
//                        }
//                    }
//                    .border(6.0.dp, Color.Gray),
//
//                contentScale = ContentScale.Crop
//            )
        }
    }
}



@Preview
@Composable
fun DemandProposeBodyPreview(){
//    BITerrand_fixTheme {
//        DemandProposeBody(
//            modifier = Modifier.fillMaxSize()
//        )
//    }
}

