package com.example.biterrand_fix.ui.DemandGet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biterrand_fix.data.DemandRepository
import com.example.biterrand_fix.data.UserBasicRepository
import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.UserBasicInfo
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

data class AggregateInfo(
    var demand: Demand,
    var userinfo: UserBasicInfo
)
sealed interface AggregateInfoUiState{
    object Loading:AggregateInfoUiState
    object Error:AggregateInfoUiState
    data class Success(val info:AggregateInfo):AggregateInfoUiState
}

class DemandGetScreenViewModel (
    savedStateHandle: SavedStateHandle,
    private val demandRepository: DemandRepository,
    private val userBasicRepository: UserBasicRepository,
): ViewModel(){
    private val demandId:Long = checkNotNull(
        savedStateHandle[DemandGetDestination.demandIdArg]
    )
    var demandGetUiState:AggregateInfoUiState  by mutableStateOf(AggregateInfoUiState.Loading)
    init {
        viewModelScope.launch {
            demandGetUiState= try {
                val demand :Demand=demandRepository.getDemandById(demandId)
                val userInfo:UserBasicInfo = userBasicRepository.getSingleUserBasicInfo(demand.requirementProposer)
               AggregateInfoUiState.Success( AggregateInfo(demand,userInfo))
            }catch (e:IOException){
                AggregateInfoUiState.Error
            }catch (e: HttpException){
                AggregateInfoUiState.Error
            }
        }
    }

}