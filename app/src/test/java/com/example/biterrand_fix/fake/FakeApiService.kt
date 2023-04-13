package com.example.biterrand_fix.fake

import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.MarsPhoto
import com.example.biterrand_fix.network.DemandsService

class FakeApiService : DemandsService {

    override suspend fun getLatestOrders(): List<Demand> {
        return FakeDemandSource.demandList
    }
    override suspend fun getOrdersBelowLaterID(id: Long): List<Demand> {
        TODO("Not yet implemented")
    }

    override suspend fun getTestPhotos(): List<MarsPhoto> {
        TODO("Not yet implemented")
    }
}