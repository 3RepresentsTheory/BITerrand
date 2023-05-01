package com.example.biterrand_fix.network

import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.MarsPhoto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface DemandsService {
    @GET("demand")
    suspend fun getLatestOrders(): List<Demand>
    @GET("demand")
    suspend fun getOrdersBelowLaterID(
        @Query("last_id") id:Long
    ):List<Demand>

    @GET("demandSpecific")
    suspend fun getOrderById(
        @Query("order_id") id:Long
    ):Demand

    @GET("photos")
    suspend fun getTestPhotos():List<MarsPhoto>

    @Multipart
    @POST("upload")
    suspend fun uploadForm(
        @Part("demand") userData: RequestBody,
        @Part image    : MultipartBody.Part?
    ):Response<RequestBody>
}

