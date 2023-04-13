package com.example.biterrand_fix

import com.example.biterrand_fix.data.NetworkDemandRepository
import com.example.biterrand_fix.fake.FakeApiService
import com.example.biterrand_fix.fake.FakeDemandSource

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test


class RepositoryTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun RepositoryTest_getLatestOrder_verifyList(){
        runTest {
            val repository = NetworkDemandRepository(
                demandApiService = FakeApiService()
            )
            assert(FakeDemandSource.demandList==repository.getDemandsList())
        }
    }
}