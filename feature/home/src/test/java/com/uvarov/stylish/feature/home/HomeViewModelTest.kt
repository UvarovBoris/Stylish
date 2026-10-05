package com.uvarov.stylish.feature.home

import app.cash.turbine.test
import com.uvarov.stylish.core.data.repository.HomeRepository
import com.uvarov.stylish.core.data.repository.WishlistRepository
import com.uvarov.stylish.core.model.BannerItem
import com.uvarov.stylish.core.model.Category
import com.uvarov.stylish.core.model.HomeFeed
import com.uvarov.stylish.core.model.Product
import io.mockk.coVerify
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
    private val wishlistRepository: WishlistRepository = mockk(relaxed = true)

    private val sampleFeed = HomeFeed(
        categories = listOf(Category("cat_1", "Beauty", "placeholder_cat_beauty")),
        heroBanners = listOf(BannerItem("hero_1", "50% OFF", "All colors")),
        products = emptyList()
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { wishlistRepository.getFavoriteProductIds() } returns flowOf(emptySet())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadHomeFeed_success_updatesUiStateToSuccess() = runTest(testDispatcher) {
        every { homeRepository.getHomeFeed() } returns flowOf(sampleFeed)

        val viewModel = HomeViewModel(homeRepository, wishlistRepository)

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

        val viewModel = HomeViewModel(homeRepository, wishlistRepository)

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
        val viewModel = HomeViewModel(homeRepository, wishlistRepository)
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
    fun heroBannerClicked_emitsNavigateToCatalogSideEffect() = runTest(testDispatcher) {
        every { homeRepository.getHomeFeed() } returns flowOf(sampleFeed)
        val viewModel = HomeViewModel(homeRepository, wishlistRepository)
        testScheduler.advanceUntilIdle()

        val banner = BannerItem("hero_1", "50% OFF", "All colors")

        viewModel.sideEffect.test {
            viewModel.onIntent(HomeIntent.HeroBannerClicked(banner))
            val effect = awaitItem()
            assertTrue(effect is HomeSideEffect.NavigateToCatalog)
            assertEquals("50% OFF", (effect as HomeSideEffect.NavigateToCatalog).title)
        }
    }

    @Test
    fun searchBarClicked_emitsNavigateToSearchSideEffect() = runTest(testDispatcher) {
        every { homeRepository.getHomeFeed() } returns flowOf(sampleFeed)
        val viewModel = HomeViewModel(homeRepository, wishlistRepository)
        testScheduler.advanceUntilIdle()

        viewModel.sideEffect.test {
            viewModel.onIntent(HomeIntent.SearchBarClicked)
            val effect = awaitItem()
            assertTrue(effect is HomeSideEffect.NavigateToSearch)
        }
    }

    @Test
    fun categoryClicked_emitsNavigateToCatalogSideEffect() = runTest(testDispatcher) {
        every { homeRepository.getHomeFeed() } returns flowOf(sampleFeed)
        val viewModel = HomeViewModel(homeRepository, wishlistRepository)
        testScheduler.advanceUntilIdle()

        viewModel.sideEffect.test {
            viewModel.onIntent(HomeIntent.CategoryClicked(categoryId = "cat_1", title = "Beauty"))
            val effect = awaitItem()
            assertTrue(effect is HomeSideEffect.NavigateToCatalog)
            assertEquals("cat_1", (effect as HomeSideEffect.NavigateToCatalog).categoryId)
            assertEquals("Beauty", effect.title)
        }
    }

    @Test
    fun toggleFavorite_callsWishlistRepository() = runTest(testDispatcher) {
        every { homeRepository.getHomeFeed() } returns flowOf(sampleFeed)
        val viewModel = HomeViewModel(homeRepository, wishlistRepository)
        testScheduler.advanceUntilIdle()

        viewModel.onIntent(HomeIntent.ToggleFavorite("prod_1"))
        testScheduler.advanceUntilIdle()

        coVerify { wishlistRepository.toggleFavorite("prod_1") }
    }
}
