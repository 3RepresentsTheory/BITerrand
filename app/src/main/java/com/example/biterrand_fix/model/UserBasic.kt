package com.example.biterrand_fix.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UserBasicInfo (
    @SerializedName("user_id")
    var userId: Long,
    @SerializedName("user_name")
    var userName:String,
    @SerializedName("avatar_url")
    var avatarUrl:String
)