package com.example.biterrand_fix.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Demand (
    //坑爹，这里因为用了gson，所有得用java的SerializedName而不是SerialName
    @SerializedName("order_id")
    var orderId: Long,

    @SerializedName("price")
    var price: Long,

    @SerializedName("start_place")
    var startPlace: String,

    @SerializedName("final_place")
    var finalPlace: String,

    @SerializedName("image_url")
    var imageUrl:String?,

    @SerializedName("order_description")
    var orderDescription: String,

    @SerializedName("requirement_proposer")
    var requirementProposer: Long,

    @SerializedName("requirement_supplier")
    var requirementSupplier: Long?,

    @SerializedName("time_stamp")
    var timestamp: String
)

@Serializable
data class MarsPhoto(
    val id: String,
    @SerialName(value = "img_src")
    val imgSrc: String
)
