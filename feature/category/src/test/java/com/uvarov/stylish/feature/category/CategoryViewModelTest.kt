package com.uvarov.stylish.feature.category

import app.cash.turbine.test
import com.uvarov.stylish.core.data.repository.ProductRepository
import com.uvarov.stylish.core.data.repository.WishlistRepository
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
class CategoryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val productRepository: ProductRepository = mockk()
    private val wishlistRepository: WishlistRepository = mockk(relaxed = true)
    private lateinit var viewModel: CategoryViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { wishlistRepository.getFavoriteProductIds() } returns flowOf(emptySet())
        viewModel = CategoryViewModel(
            productRepository = productRepository,
            wishlistRepository = wishlistRepository,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isLoading() {
        assertEquals(CategoryUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun loadCategory_success_emitsSuccessState() = runTest(testDispatcher) {
        val sampleProducts = listOf(
            Product(
                id = "p1",
                title = "Product 1",
                description = "Desc",
                currentPrice = 100,
                originalPrice = 200,
                discountPercent = 50,
                rating = 4.5f,
                reviewCount = 10,
                imageUrl = "url",
                categoryId = "cat_womens"
            )
        )
        every { productRepository.getProductsByCategory("cat_womens") } returns flowOf(sampleProducts)

        viewModel.uiState.test {
            assertEquals(CategoryUiState.Loading, awaitItem())

            viewModel.onIntent(CategoryIntent.LoadCategory("cat_womens", "Womens"))
            testScheduler.advanceUntilIdle()

            val success = awaitItem() as CategoryUiState.Success
            assertEquals("cat_womens", success.categoryId)
            assertEquals("Womens", success.categoryTitle)
            assertEquals(sampleProducts, success.products)

            // Re-sending same LoadCategory should not emit Loading again
            viewModel.onIntent(CategoryIntent.LoadCategory("cat_womens", "Womens"))
            testScheduler.advanceUntilIdle()
            expectNoEvents()
        }
    }

    @Test
    fun loadCategory_failure_emitsErrorState() = runTest(testDispatcher) {
        every { productRepository.getProductsByCategory("cat_womens") } returns flow {
            throw RuntimeException("Network error")
        }

        viewModel.uiState.test {
            assertEquals(CategoryUiState.Loading, awaitItem())

            viewModel.onIntent(CategoryIntent.LoadCategory("cat_womens", "Womens"))
            testScheduler.advanceUntilIdle()

            val error = awaitItem() as CategoryUiState.Error
            assertEquals("Network error", error.message)
        }
    }

    @Test
    fun backClicked_emitsNavigateBackSideEffect() = runTest(testDispatcher) {
        viewModel.sideEffect.test {
            viewModel.onIntent(CategoryIntent.BackClicked)
            testScheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is CategorySideEffect.NavigateBack)
        }
    }

    @Test
    fun productClicked_emitsNavigateToProductDetailsSideEffect() = runTest(testDispatcher) {
        val product = Product(
            id = "p1",
            title = "Product 1",
            description = "Desc",
            currentPrice = 100,
            originalPrice = 200,
            discountPercent = 50,
            rating = 4.5f,
            reviewCount = 10,
            imageUrl = "url"
        )
        viewModel.sideEffect.test {
            viewModel.onIntent(CategoryIntent.ProductClicked(product))
            testScheduler.advanceUntilIdle()

            val effect = awaitItem() as CategorySideEffect.NavigateToProductDetails
            assertEquals("p1", effect.productId)
        }
    }

    @Test
    fun toggleFavorite_callsWishlistRepository() = runTest(testDispatcher) {
        viewModel.onIntent(CategoryIntent.ToggleFavorite("p1"))
        testScheduler.advanceUntilIdle()

        coVerify { wishlistRepository.toggleFavorite("p1") }
    }
}
