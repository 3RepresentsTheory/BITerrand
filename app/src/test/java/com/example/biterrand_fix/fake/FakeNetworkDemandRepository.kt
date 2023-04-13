package com.example.biterrand_fix.fake

import com.example.biterrand_fix.data.DemandRepository
import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.MarsPhoto

class FakeNetworkDemandRepository:DemandRepository {
    override suspend fun getDemandsList(): List<Demand> {
        return FakeDemandSource.demandList
    }

    override suspend fun getDemandsListAfterId(lastId: Long): List<Demand> {
        TODO("Not yet implemented")
    }

    override suspend fun getTestMarsPhotos(): List<MarsPhoto> {
        TODO("Not yet implemented")
    }
}