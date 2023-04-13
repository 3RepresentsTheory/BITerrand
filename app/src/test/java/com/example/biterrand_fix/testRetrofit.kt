package com.example.biterrand_fix

import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.MarsPhoto
import com.example.biterrand_fix.ui.ErrandScreenViewModel
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//private val BASE_URL = "http://43.138.15.233:8946/"
//
////    private val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"
//
//private val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
//
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(GsonConverterFactory.create(gson))
//    .baseUrl(BASE_URL)
//    .build()
//
//@Serializable
//data class Demand (
//    //坑爹，这里因为用了gson，所有得用java的SerializedName而不是SerialName
//    @SerializedName("order_id")
//    var orderId: Long,
//
//    @SerializedName("price")
//    var price: Long,
//
//    @SerializedName("start_place")
//    var startPlace: String,
//
//    @SerializedName("final_place")
//    var finalPlace: String,
//
//    @SerializedName("image_url")
//    var imageUrl:String,
//
//    @SerializedName("order_description")
//    var orderDescription: String,
//
//    @SerializedName("requirement_proposer")
//    var requirementProposer: Long,
//
//    @SerializedName("requirement_supplier")
//    var requirementSupplier: Long,
//
//    @SerializedName("time_stamp")
//    var timestamp: String
//)


//interface DemandsService {
//    @GET("demand")
//    suspend fun getLatestOrders(): List<Demand>
//    @GET("demand")
//    suspend fun getOrdersBelowLaterID(
//        @Query("last_id") id:Long
//    ):List<Demand>
//
//
//    @GET("photos")
//    suspend fun getTestPhotos():List<MarsPhoto>
//}

//
//fun main() = runBlocking{
//    val retrofitService = retrofit.create(DemandsService::class.java)
//    val retList         = retrofitService.getLatestOrders()
//    println(retList)
//}
//

fun main(){
    println(UserFriendlyTime("2023-04-10 12:00:01"))


}

fun UserFriendlyTime(
    time_stamp:String
):String{
    val time_stamp_date         :LocalDate  = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(time_stamp))
    val time_stamp_date_time: LocalDateTime = LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(time_stamp))
    when(time_stamp_date){
        LocalDate.now()
        -> return "今天 "+time_stamp_date_time.toLocalTime()
        LocalDate.now().minusDays(1)
        -> return "昨天 " +time_stamp_date_time.toLocalTime()
        else
        -> return time_stamp
    }
}
