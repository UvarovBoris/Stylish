package com.uvarov.stylish.feature.main

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialUiState_startsAtHomeTab() {
        val state = viewModel.uiState.value
        assertEquals(MainTab.HOME, state.currentTab)
    }

    @Test
    fun tabSelectedIntent_updatesCurrentTab() = runTest(testDispatcher) {
        viewModel.uiState.test {
            // Initial item
            assertEquals(MainTab.HOME, awaitItem().currentTab)

            // Select Wishlist
            viewModel.onIntent(MainIntent.TabSelected(MainTab.WISHLIST))
            assertEquals(MainTab.WISHLIST, awaitItem().currentTab)

            // Select Cart
            viewModel.onIntent(MainIntent.TabSelected(MainTab.CART))
            assertEquals(MainTab.CART, awaitItem().currentTab)

            // Select Search
            viewModel.onIntent(MainIntent.TabSelected(MainTab.SEARCH))
            assertEquals(MainTab.SEARCH, awaitItem().currentTab)

            // Select Settings
            viewModel.onIntent(MainIntent.TabSelected(MainTab.SETTINGS))
            assertEquals(MainTab.SETTINGS, awaitItem().currentTab)

            // Return to Home
            viewModel.onIntent(MainIntent.TabSelected(MainTab.HOME))
            assertEquals(MainTab.HOME, awaitItem().currentTab)
        }
    }
}
