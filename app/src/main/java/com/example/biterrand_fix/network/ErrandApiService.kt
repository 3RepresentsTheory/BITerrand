package com.example.biterrand_fix.network

import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.MarsPhoto
import retrofit2.http.GET
import retrofit2.http.Query

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
}

