package com.example.biterrand_fix.network

import com.example.biterrand_fix.model.UserBasicInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface UserBasicService {
    @GET("user")
    suspend fun getUserBasicInfo(
        @Query("user_id") id:Long
    ):UserBasicInfo
}
