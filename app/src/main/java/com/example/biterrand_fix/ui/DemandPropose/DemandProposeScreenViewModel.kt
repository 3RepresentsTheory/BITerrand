package com.example.biterrand_fix.ui.DemandPropose

import android.app.Service
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biterrand_fix.data.DemandRepository
import com.example.biterrand_fix.data.UserBasicRepository
import com.example.biterrand_fix.model.Demand
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception


data class proposeUiState(
    val demandInfo: Demand = Demand(
        orderId = 0,
        price = 0,
        startPlace = "",
        finalPlace = "",
        imageUrl = null,
        orderDescription = "",
        requirementProposer = 1,
        requirementSupplier = null,
        timestamp = "",
        timeout = ""
    ),
    val photoUiState: photoUiState = photoUiState(),
    var isFormValid:Boolean = false,

    //this seems useless, delete it if wouldn't use
    var isCameraGranted  :Boolean = false,
)

data class photoUiState(
    var isPhotoAdded:Boolean = false,
    var addPhotoUrl: Uri? = null
)

/**
 * here need some technic to save the user info ,replace the requiement proposer,
 * and we have to guarantee there wouldn't be someone impersonate the client to send
 * demand, we will use simple register mechanism first
 */

class DemandProposeScreenViewModel (
    savedStateHandle: SavedStateHandle,
    private val demandRepository: DemandRepository,
    private val userBasicRepository: UserBasicRepository,
): ViewModel(){
    var proposeUiState by mutableStateOf(proposeUiState())
        private  set

    var latestUsedUri:Uri? by mutableStateOf(null)
        private set

    suspend fun proposeDemand(){
        /*TODO 这里需要一个错误处理，以防表单没有正常提交*/
        Log.d("TDEBUG","${proposeUiState.demandInfo}")
        if(validateInput(proposeUiState.demandInfo)){
            try {
                demandRepository.uploadDemand(proposeUiState.demandInfo)
            }catch (e:Exception){
                Log.d(proposeDebugTag,"${e}")
            }
        }else{
            proposeUiState.isFormValid=false
        }
    }
    fun updateUiState(demand:Demand){
        Log.d(proposeDebugTag,"update demand info")
        proposeUiState = proposeUiState.copy(
            demandInfo = demand,
            isFormValid =validateInput(demand),
        )
    }
    fun updateUiImageState(){
        Log.d(proposeDebugTag,"update photo info")
        proposeUiState = proposeUiState.copy(
            photoUiState = photoUiState(
                isPhotoAdded = true,
                addPhotoUrl = latestUsedUri
            )
        )
        proposeUiState.demandInfo.imageUrl= latestUsedUri.toString()
    }

    fun uploadLocalImage(url:Uri){
        latestUsedUri = url
        updateUiImageState()
    }

    fun createTempFileAndLoadIntent(context: Context):Intent{
        Log.d(proposeDebugTag,"create a temp file and load intent")
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val tempFile = File.createTempFile("tempPicture", ".png", context.cacheDir)
        Log.d(proposeDebugTag,"temp file locate in :${tempFile}")
        latestUsedUri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            tempFile
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, latestUsedUri);
        return takePictureIntent
    }
    /**
     * sheet state
     */
    @OptIn(ExperimentalMaterialApi::class)
    val sheetState = ModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    private fun validateInput(
        uiInputForm:Demand = proposeUiState.demandInfo
    ): Boolean {
        return with(uiInputForm){
            price>0 &&
            startPlace.isNotBlank() &&
            finalPlace.isNotBlank() &&
            orderDescription.isNotBlank() &&
            timeout.isNotBlank()
        }
    }

    var isHoldOnServiceRunning by mutableStateOf(false)
        private set

    fun startHoldOnService(context: Context){
        val intent = Intent(context,HoldOnService::class.java)
        context.startService(intent)
        isHoldOnServiceRunning = true
    }

    fun stopHoldOnService(context: Context){
        val intent = Intent(context,HoldOnService::class.java)
        context.stopService(intent)
        isHoldOnServiceRunning = false
    }



}


/**
 * background service used to prevent being kill after onStop when using camera and facing
 * system killing process for high memory demand
 */
class HoldOnService : Service() {
    // 定义一个标记来表示 Service 是否在运行
    private var isRunning = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 在 onStartCommand 方法中执行任务逻辑
        isRunning = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isRunning) {
                Log.d(proposeDebugTag, "prevent being killed when taking photos in ${Thread.currentThread().name}")
                // 使用 delay() 函数等待 1 秒钟
                delay(1000)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        // 返回 null 表示 Service 不支持绑定
        return null
    }

    override fun onDestroy() {
        // 在 onDestroy 方法中停止任务逻辑
        Log.d(proposeDebugTag,"destroy the hold service")
        isRunning = false
        super.onDestroy()
    }
}