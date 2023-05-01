package com.example.biterrand_fix.ui.DemandPropose

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.biterrand_fix.data.DemandRepository
import com.example.biterrand_fix.data.UserBasicRepository
import com.example.biterrand_fix.model.Demand




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
    val isFormValid:Boolean = false
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

    suspend fun proposeDemand(){
        /*TODO 这里需要一个错误处理，以防表单没有正常提交*/
        Log.d("TDEBUG","${proposeUiState.demandInfo}")
        if(validateInput(proposeUiState.demandInfo)){
            demandRepository.uploadDemand(proposeUiState.demandInfo)
        }
    }

    fun updateUiState(demand:Demand){
        proposeUiState = proposeUiState(
            demandInfo = demand,
            isFormValid =validateInput(demand)
        )
    }

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

