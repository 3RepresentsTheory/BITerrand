package com.example.biterrand_fix.data

import com.example.biterrand_fix.model.UserBasicInfo
import com.example.biterrand_fix.network.UserBasicService

interface UserBasicRepository{

    suspend fun getSingleUserBasicInfo(id:Long):UserBasicInfo

}


class NetworkUserBasicInfoRepository(val userapi: UserBasicService):UserBasicRepository{

    override suspend fun getSingleUserBasicInfo(id:Long): UserBasicInfo {
        return userapi.getUserBasicInfo(id);
    }
}