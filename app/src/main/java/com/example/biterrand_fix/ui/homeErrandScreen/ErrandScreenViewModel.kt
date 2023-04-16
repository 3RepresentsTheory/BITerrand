package com.example.biterrand_fix.ui

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.biterrand_fix.BITerrandApplication
import com.example.biterrand_fix.data.DemandRepository
import com.example.biterrand_fix.data.UserBasicRepository
import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.UserBasicInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


sealed interface ErrandUiState{

    data class Success(val demands: List<Demand>):ErrandUiState
    data class Error(val exceptionDescription:String):ErrandUiState
    //        object Error:ErrandUiState
    //need fine grained loading state
    object Loading:ErrandUiState
}




class ErrandScreenViewModel(
    private val demandRepository: DemandRepository,
    private val userBasicRepository: UserBasicRepository,
): ViewModel(){
    /**
     * used for swipe refresh on the top
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    var lastDemandId: Long by mutableStateOf(0)

    //should be modified later, it 's dangerous because some time when the
    //network failed , it would read old stale data
    var errandUiList:List<Demand> by mutableStateOf(listOf())
        private set

    var errandUiState: ErrandUiState by mutableStateOf(ErrandUiState.Loading)
        private set


    fun getNewestDemandList(){
        viewModelScope.launch {
            _isLoading.value= true

            errandUiState = ErrandUiState.Loading
            errandUiState = try {
                /**
                 * wait to modify here
                 */
                Log.d("TDEBUG","call get demand list")
//                val  listResult = demandRepository.getTestMarsPhotos()
                val  listResult = demandRepository.getDemandsList()
                Log.d("TDEBUG","demand list return")
                println(listResult)
                errandUiList=listResult
                ErrandUiState.Success(listResult)
            } catch (e: IOException){
                ErrandUiState.Error("IOException with ${e}")
            } catch (e: HttpException){
                ErrandUiState.Error("HTTPException with ${e}")
            }

            _isLoading.value= false
        }
    }
    fun getDemandListAfterId(id:Long){
        viewModelScope.launch {
            _isLoading.value= true

            errandUiState = ErrandUiState.Loading
            errandUiState = try {
                /**
                 * wait to modify here
                 */
                Log.d("TDEBUG","call get demand list")
//                val  listResult = demandRepository.getTestMarsPhotos()
                val  listResult = demandRepository.getDemandsListAfterId(id)
                Log.d("TDEBUG","demand list return")
                println(listResult)
                errandUiList=listResult
                ErrandUiState.Success(listResult)
            } catch (e: IOException){
                ErrandUiState.Error("IOException with ${e}")
            } catch (e: HttpException){
                ErrandUiState.Error("HTTPException with ${e}")
            }

            _isLoading.value= false
        }

    }

    init {
        getNewestDemandList()
    }

    fun getUserNameAvatar(userid:Long):UserBasicInfo{
        var result:UserBasicInfo = defaultUserInfo
        viewModelScope.launch {
            result=
                userBasicRepository.getSingleUserBasicInfo(userid)
            /**
             * we aggressively believe it will get the result from net work
             */
        }
        return result
    }

    val defaultUserInfo :UserBasicInfo = UserBasicInfo(
        userId  =0,
        userName= "...",
        avatarUrl = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"
    )


    //here we use factory to make viewmodel more reusable, more information can
    //click here :https://stackoverflow.com/questions/67985585/why-do-we-need-viewmodelprovider-factory-to-pass-view-model-to-a-screen
    //On the method mentioned in the link, we can use applicationContext to get the
    //member container in Application() but that wasn't a good choice.
    //we can use viewModel function to provide reusable viewmodel
    //https://developer.android.com/jetpack/compose/libraries#viewmodel

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory{
            initializer {
                Log.d("TDEBUG","${this} ${this[APPLICATION_KEY]} and ${APPLICATION_KEY}")
                val application = (this[APPLICATION_KEY] as BITerrandApplication)
                application_ref = application
                ErrandScreenViewModel(
                    demandRepository = application.container.demandRepository,
                    userBasicRepository = application.container.userbasicinfoRepository
                )
            }
        }
        //I cannot figure out how to get the instance of application here
        lateinit var application_ref:BITerrandApplication
    }
}


fun UserFriendlyTime(
     time_stamp:String
):String{
    val time_stamp_date         :LocalDate  = LocalDate.from(ErrandScreenViewModel.application_ref.dateFormatter.parse(time_stamp))
    val time_stamp_date_time: LocalDateTime = LocalDateTime.from(ErrandScreenViewModel.application_ref.dateFormatter.parse(time_stamp))
    when(time_stamp_date){
        ErrandScreenViewModel.application_ref.currentDate
            -> return "今天 "+time_stamp_date_time.toLocalTime().withSecond(0)
        ErrandScreenViewModel.application_ref.currentDate.minusDays(1)
            -> return "昨天 " +time_stamp_date_time.toLocalTime().withSecond(0)
        else
            -> return time_stamp_date_time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }
}

fun isOverDue(
    time_stamp: String
):Boolean{
    val time_stamp_date_time: LocalDateTime = LocalDateTime.from(ErrandScreenViewModel.application_ref.dateFormatter.parse(time_stamp))
    return time_stamp_date_time.isBefore(LocalDateTime.now())
}

fun DDLTime(
    time_stamp: String
):String{
    val time_stamp_date         :LocalDate  = LocalDate.from(ErrandScreenViewModel.application_ref.dateFormatter.parse(time_stamp))
    val time_stamp_date_time: LocalDateTime = LocalDateTime.from(ErrandScreenViewModel.application_ref.dateFormatter.parse(time_stamp))
    when(time_stamp_date){
        ErrandScreenViewModel.application_ref.currentDate
        -> return "今天 "+time_stamp_date_time.toLocalTime().withSecond(0)
        ErrandScreenViewModel.application_ref.currentDate.minusDays(1)
        -> return "昨天 " +time_stamp_date_time.toLocalTime().withSecond(0)
        else
        -> return time_stamp_date_time.format(DateTimeFormatter.ofPattern("MM-dd HH:mm"))
        //we have to forbid user to choose ddl later than 30 days, here we neglect the year, even in December
    }
}