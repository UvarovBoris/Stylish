package com.uvarov.stylish.feature.home

import app.cash.turbine.test
import com.uvarov.stylish.core.data.repository.HomeRepository
import com.uvarov.stylish.core.model.BannerItem
import com.uvarov.stylish.core.model.Category
import com.uvarov.stylish.core.model.DealOfTheDay
import com.uvarov.stylish.core.model.HomeFeed
import com.uvarov.stylish.core.model.Product
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val homeRepository: HomeRepository = mockk()

    private val sampleFeed = HomeFeed(
        categories = listOf(Category("cat_1", "Beauty", "placeholder_cat_beauty")),
        heroBanners = listOf(BannerItem("hero_1", "50% OFF", "All colors")),
        dealOfTheDay = DealOfTheDay("Deal of the Day", 3600, emptyList()),
        specialOfferBanner = BannerItem("sp_1", "Special", "Desc"),
        heelsBanner = BannerItem("h_1", "Heels", "Desc"),
        trendingProducts = emptyList(),
        newArrivalsBanner = BannerItem("na_1", "New Arrivals", "Desc"),
        sponsoredBanner = BannerItem("spon_1", "Sponsored", "Desc")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadHomeFeed_success_updatesUiStateToSuccess() = runTest(testDispatcher) {
        every { homeRepository.getHomeFeed() } returns flowOf(sampleFeed)

        val viewModel = HomeViewModel(homeRepository)

        viewModel.uiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            val successState = awaitItem()
            assertTrue(successState is HomeUiState.Success)
            assertEquals(sampleFeed, (successState as HomeUiState.Success).homeFeed)
        }
    }

    @Test
    fun loadHomeFeed_error_updatesUiStateToError() = runTest(testDispatcher) {
        every { homeRepository.getHomeFeed() } returns flow { error("Network failed") }

        val viewModel = HomeViewModel(homeRepository)

        viewModel.uiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            testScheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertTrue(errorState is HomeUiState.Error)
            assertEquals("Network failed", (errorState as HomeUiState.Error).message)
        }
    }

    @Test
    fun productClicked_emitsNavigateToProductDetailsSideEffect() = runTest(testDispatcher) {
        every { homeRepository.getHomeFeed() } returns flowOf(sampleFeed)
        val viewModel = HomeViewModel(homeRepository)
        testScheduler.advanceUntilIdle()

        val product = Product("prod_1", "Dress", "Desc", 100, 200, 50, 4.0f, 10, "placeholder")

        viewModel.sideEffect.test {
            viewModel.onIntent(HomeIntent.ProductClicked(product))
            val effect = awaitItem()
            assertTrue(effect is HomeSideEffect.NavigateToProductDetails)
            assertEquals("prod_1", (effect as HomeSideEffect.NavigateToProductDetails).productId)
        }
    }

    @Test
    fun searchQueryChanged_updatesSearchQueryInUiState() = runTest(testDispatcher) {
        every { homeRepository.getHomeFeed() } returns flowOf(sampleFeed)
        val viewModel = HomeViewModel(homeRepository)
        testScheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState is HomeUiState.Success)

            viewModel.onIntent(HomeIntent.SearchQueryChanged("Sneakers"))
            val updatedState = awaitItem()
            assertTrue(updatedState is HomeUiState.Success)
            assertEquals("Sneakers", (updatedState as HomeUiState.Success).searchQuery)
        }
    }
}
