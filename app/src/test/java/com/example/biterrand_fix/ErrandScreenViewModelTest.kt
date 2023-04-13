package com.example.biterrand_fix

import com.example.biterrand_fix.fake.FakeNetworkDemandRepository
import com.example.biterrand_fix.rule.TestDispatcherRule
import com.example.biterrand_fix.ui.ErrandScreenViewModel
import com.example.biterrand_fix.ui.ErrandUiState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ErrandScreenViewModelTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun ErrandScreenViewModelTest_getDemandList_verifyErrandUiStateSuccess(){
        runTest {
            val errandScreenViewModel = ErrandScreenViewModel(
                demandRepository = FakeNetworkDemandRepository()
            )
            assertEquals(
                ErrandUiState.Success("Success with 2"),
                errandScreenViewModel.errandUiState
            )
        }
    }
}