package com.example.biterrand_fix.ui

import android.util.Log
import androidx.compose.runtime.produceState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.biterrand_fix.BITerrandApplication
import com.example.biterrand_fix.ui.DemandGet.DemandGetDestination
import com.example.biterrand_fix.ui.DemandGet.DemandGetScreenViewModel
import com.example.biterrand_fix.ui.DemandPropose.DemandProposeScreenViewModel
import com.example.biterrand_fix.ui.DemandPropose.proposeDebugTag

object AppViewModelProvider{
    val Factory = viewModelFactory {
        initializer {
            DemandGetScreenViewModel(
                this.createSavedStateHandle(),
                ErrandApplication().container.demandRepository,
                ErrandApplication().container.userbasicinfoRepository
            )
        }
        initializer {
            Log.d(proposeDebugTag,"Demand propose viewmodel create")
            DemandProposeScreenViewModel(
                this.createSavedStateHandle(),
                ErrandApplication().container.demandRepository,
                ErrandApplication().container.userbasicinfoRepository
            )
        }
    }
}

fun CreationExtras.ErrandApplication():BITerrandApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BITerrandApplication)