package com.example.biterrand_fix.fake

import com.example.biterrand_fix.model.Demand
import com.google.gson.annotations.SerializedName

object FakeDemandSource {
    const val One_orderId: Long = 1
    const val One_price: Long   = 250
    const val One_startPlace: String = "北京理工大学东校区甘棠园"
    const val One_finalPlace: String = "北京理工大学北校区"
    const val One_imageUrl:String    = "https://img0.baidu.com/it/u=1318541766,1632186299&fm=253&app=120&f=JPEG?w=480&h=600"
    const val One_orderDescription: String  = "好康的"
    const val One_requirementProposer: Long = 250
    const val One_requirementSupplier: Long = 258
    const val One_timestamp: String         = "2022-04-08 23:00:00"

    const val Two_orderId: Long = 1
    const val Two_price: Long   = 250
    const val Two_startPlace: String = "北京理工大学东校区甘棠园"
    const val Two_finalPlace: String = "北京理工大学北校区"
    const val Two_imageUrl:String    = "https://img0.baidu.com/it/u=3544811188,565749833&fm=253&fmt=auto&app=120&f=JPEG?w=289&h=358"
    const val Two_orderDescription: String  = "好康的"
    const val Two_requirementProposer: Long = 250
    const val Two_requirementSupplier: Long = 258
    const val Two_timestamp: String         = "2022-04-08 23:00:00"

    val demandList = listOf(
        Demand(
            orderId =               One_orderId,
            price =                 One_price,
            startPlace =            One_startPlace,
            finalPlace          =   One_finalPlace         ,
            imageUrl            =   One_imageUrl           ,
            orderDescription    =   One_orderDescription   ,
            requirementProposer =   One_requirementProposer,
            requirementSupplier =   One_requirementSupplier,
            timestamp           =   One_timestamp          ,
        ),

        Demand(
            orderId =               Two_orderId,
            price =                 Two_price,
            startPlace =            Two_startPlace,
            finalPlace          =   Two_finalPlace         ,
            imageUrl            =   Two_imageUrl           ,
            orderDescription    =   Two_orderDescription   ,
            requirementProposer =   Two_requirementProposer,
            requirementSupplier =   Two_requirementSupplier,
            timestamp           =   Two_timestamp          ,
        )


    )
}