package com.example.biterrand_fix.data

import com.example.biterrand_fix.model.Demand
import com.google.gson.annotations.SerializedName

object FakeDemandSource {
    const val One_orderId: Long = 1
    const val One_price: Long   = 250
    const val One_startPlace: String = "良乡东校区文教H楼233教室某地下室就是特别长你看咋样"
    const val One_finalPlace: String = "北京理工大学北校区"
     val One_imageUrl:String?   = "https://img0.baidu.com/it/u=1318541766,1632186299&fm=253&app=120&f=JPEG?w=480&h=600"
    const val One_orderDescription: String  = "好康的"
    const val One_requirementProposer: Long = 250
    const val One_requirementSupplier: Long = 258
    const val One_timestamp: String         = "2023-04-12 23:00:01"

    const val Two_orderId: Long = 1
    const val Two_price: Long   = 250
    const val Two_startPlace: String = "北京理工大学东校区甘棠园"
    const val Two_finalPlace: String = "北京理工大学北校区"
//     val Two_imageUrl:String?    = "https://img0.baidu.com/it/u=3544811188,565749833&fm=253&fmt=auto&app=120&f=JPEG?w=289&h=358"
     val Two_imageUrl:String?    = null
    const val Two_orderDescription: String  = "曾经有教育家做了一个实验，给中国孩子和美国孩子一杯水，让他们不用制冷器就让水结冰。中国孩子使用甘雨开E直接把水冻结了，赢。而美国孩子不玩原神，直接认输。从小培养玩原神的意识，比任何教育都重要"
    const val Two_requirementProposer: Long = 250
    const val Two_requirementSupplier: Long = 258
    const val Two_timestamp: String         = "2023-04-10 23:00:00"

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
            requirementSupplier =   null,
            timestamp           =   Two_timestamp          ,
        )


    )
}