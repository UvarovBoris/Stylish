package com.uvarov.stylish

import app.cash.turbine.test
import com.uvarov.stylish.core.data.repository.UserDataRepository
import com.uvarov.stylish.feature.main.navigation.MainRoute
import com.uvarov.stylish.feature.onboarding.navigation.OnboardingRoute
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val userDataRepository = mockk<UserDataRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun whenOnboardingNotCompleted_startDestinationIsOnboardingRoute() = runTest(testDispatcher) {
        every { userDataRepository.isOnboardingCompleted } returns flowOf(false)

        val viewModel = MainActivityViewModel(userDataRepository)

        viewModel.uiState.test {
            assertEquals(MainActivityUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            assertEquals(MainActivityUiState.Success(OnboardingRoute), awaitItem())
        }
    }

    @Test
    fun whenOnboardingCompleted_startDestinationIsMainRoute() = runTest(testDispatcher) {
        every { userDataRepository.isOnboardingCompleted } returns flowOf(true)

        val viewModel = MainActivityViewModel(userDataRepository)

        viewModel.uiState.test {
            assertEquals(MainActivityUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            assertEquals(MainActivityUiState.Success(MainRoute), awaitItem())
        }
    }
}
