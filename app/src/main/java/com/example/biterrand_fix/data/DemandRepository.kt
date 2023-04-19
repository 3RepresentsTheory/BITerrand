package com.example.biterrand_fix.data

import com.example.biterrand_fix.model.Demand
import com.example.biterrand_fix.model.MarsPhoto
import com.example.biterrand_fix.network.DemandsService
import org.jetbrains.annotations.TestOnly

/**
 *  The Repository for Demands
 */
interface DemandRepository {
    suspend fun getDemandsList():List<Demand>

    suspend fun getDemandsListAfterId(lastId:Long):List<Demand>

    suspend fun getDemandById(id:Long):Demand

    @TestOnly
//   use for test the sample data
    suspend fun getTestMarsPhotos():List<MarsPhoto>
}



/**
 * Network data source Repository, for uncoupling, we can use DI instead of hard
 * coding the ApiService in the NetworkRepository (Perhaps we use local data source/ different
 * network library)
 */
class NetworkDemandRepository(val demandApiService: DemandsService):DemandRepository{
    override suspend fun getDemandsList(): List<Demand> {
        return demandApiService.getLatestOrders()
    }

    override suspend fun getDemandsListAfterId(lastId: Long): List<Demand> {
        return demandApiService.getOrdersBelowLaterID(lastId)
    }

    override suspend fun getDemandById(id: Long): Demand {
        return demandApiService.getOrderById(id)
    }

    @TestOnly
    override suspend fun getTestMarsPhotos(): List<MarsPhoto> {
        return demandApiService.getTestPhotos()
    }
}

