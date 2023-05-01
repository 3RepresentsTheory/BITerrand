package com.example.biterrand_fix.ui.DemandPropose

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.biterrand_fix.data.DemandRepository
import com.example.biterrand_fix.data.UserBasicRepository
import com.example.biterrand_fix.model.Demand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


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
            demandRepository.uploadDemand(proposeUiState.demandInfo)
        }
    }

    fun updateUiState(demand:Demand){
        proposeUiState = proposeUiState.copy(
            demandInfo = demand,
            isFormValid =validateInput(demand),
        )
    }
    fun updateUiImageState(){
        proposeUiState = proposeUiState.copy(
            photoUiState = photoUiState(
                isPhotoAdded = true,
                addPhotoUrl = latestUsedUri
            )
        )
        proposeUiState.demandInfo.imageUrl="yes!"
    }

    fun createTempFileAndLoadIntent(context: Context):Intent{
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val tempFile = File.createTempFile("tempPicture", ".png", context.cacheDir)
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




}


