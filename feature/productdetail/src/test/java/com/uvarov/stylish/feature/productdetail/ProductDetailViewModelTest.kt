package com.uvarov.stylish.feature.productdetail

import app.cash.turbine.test
import com.uvarov.stylish.core.data.repository.ProductRepository
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val productRepository: ProductRepository = mockk()
    private lateinit var viewModel: ProductDetailViewModel

    private val sampleProduct = Product(
        id = "prod_1",
        title = "Nike Sneakers",
        description = "Vision Alta Men's Shoes",
        currentPrice = 1500,
        originalPrice = 2999,
        discountPercent = 50,
        rating = 4.5f,
        reviewCount = 56890,
        imageUrl = "https://api.stylish.app/images/sneakers.jpg",
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ProductDetailViewModel(productRepository = productRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isLoading() {
        assertEquals(ProductDetailUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun loadProduct_success_emitsSuccessState() = runTest(testDispatcher) {
        every { productRepository.getProductById("prod_1") } returns flowOf(sampleProduct)

        viewModel.uiState.test {
            assertEquals(ProductDetailUiState.Loading, awaitItem())

            viewModel.onIntent(ProductDetailIntent.LoadProduct("prod_1"))
            testScheduler.advanceUntilIdle()

            val success = awaitItem() as ProductDetailUiState.Success
            assertEquals(sampleProduct, success.product)
            assertEquals("7 UK", success.selectedSize)
            assertFalse(success.isDescriptionExpanded)

            // Re-sending same LoadProduct should not re-trigger Loading
            viewModel.onIntent(ProductDetailIntent.LoadProduct("prod_1"))
            testScheduler.advanceUntilIdle()
            expectNoEvents()
        }
    }

    @Test
    fun loadProduct_failure_emitsErrorState() = runTest(testDispatcher) {
        every { productRepository.getProductById("prod_1") } returns flow {
            throw RuntimeException("Product not found")
        }

        viewModel.uiState.test {
            assertEquals(ProductDetailUiState.Loading, awaitItem())

            viewModel.onIntent(ProductDetailIntent.LoadProduct("prod_1"))
            testScheduler.advanceUntilIdle()

            val error = awaitItem() as ProductDetailUiState.Error
            assertEquals("Product not found", error.message)
        }
    }

    @Test
    fun selectSize_updatesSelectedSize() = runTest(testDispatcher) {
        every { productRepository.getProductById("prod_1") } returns flowOf(sampleProduct)

        viewModel.uiState.test {
            assertEquals(ProductDetailUiState.Loading, awaitItem())

            viewModel.onIntent(ProductDetailIntent.LoadProduct("prod_1"))
            testScheduler.advanceUntilIdle()

            val initialSuccess = awaitItem() as ProductDetailUiState.Success
            assertEquals("7 UK", initialSuccess.selectedSize)

            viewModel.onIntent(ProductDetailIntent.SelectSize("9 UK"))
            val updated = awaitItem() as ProductDetailUiState.Success
            assertEquals("9 UK", updated.selectedSize)
        }
    }

    @Test
    fun toggleDescription_togglesExpansion() = runTest(testDispatcher) {
        every { productRepository.getProductById("prod_1") } returns flowOf(sampleProduct)

        viewModel.uiState.test {
            assertEquals(ProductDetailUiState.Loading, awaitItem())

            viewModel.onIntent(ProductDetailIntent.LoadProduct("prod_1"))
            testScheduler.advanceUntilIdle()

            val initialSuccess = awaitItem() as ProductDetailUiState.Success
            assertFalse(initialSuccess.isDescriptionExpanded)

            viewModel.onIntent(ProductDetailIntent.ToggleDescription)
            val expanded = awaitItem() as ProductDetailUiState.Success
            assertTrue(expanded.isDescriptionExpanded)

            viewModel.onIntent(ProductDetailIntent.ToggleDescription)
            val collapsed = awaitItem() as ProductDetailUiState.Success
            assertFalse(collapsed.isDescriptionExpanded)
        }
    }

    @Test
    fun backClicked_emitsNavigateBackSideEffect() = runTest(testDispatcher) {
        viewModel.sideEffect.test {
            viewModel.onIntent(ProductDetailIntent.BackClicked)
            testScheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is ProductDetailSideEffect.NavigateBack)
        }
    }

    @Test
    fun cartClicked_emitsNavigateToCartSideEffect() = runTest(testDispatcher) {
        viewModel.sideEffect.test {
            viewModel.onIntent(ProductDetailIntent.CartClicked)
            testScheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is ProductDetailSideEffect.NavigateToCart)
        }
    }

    @Test
    fun addToCartClicked_emitsToastSideEffect() = runTest(testDispatcher) {
        viewModel.sideEffect.test {
            viewModel.onIntent(ProductDetailIntent.AddToCartClicked)
            testScheduler.advanceUntilIdle()

            val effect = awaitItem() as ProductDetailSideEffect.ShowToast
            assertEquals("Added to cart", effect.message)
        }
    }

    @Test
    fun buyNowClicked_emitsToastSideEffect() = runTest(testDispatcher) {
        viewModel.sideEffect.test {
            viewModel.onIntent(ProductDetailIntent.BuyNowClicked)
            testScheduler.advanceUntilIdle()

            val effect = awaitItem() as ProductDetailSideEffect.ShowToast
            assertEquals("Proceeding to checkout", effect.message)
        }
    }

    @Test
    fun retryClicked_reloadsProduct() = runTest(testDispatcher) {
        every { productRepository.getProductById("prod_1") } returns flow {
            throw RuntimeException("Network error")
        } andThen flowOf(sampleProduct)

        viewModel.uiState.test {
            assertEquals(ProductDetailUiState.Loading, awaitItem())

            viewModel.onIntent(ProductDetailIntent.LoadProduct("prod_1"))
            testScheduler.advanceUntilIdle()

            val error = awaitItem() as ProductDetailUiState.Error
            assertEquals("Network error", error.message)

            viewModel.onIntent(ProductDetailIntent.RetryClicked)
            testScheduler.advanceUntilIdle()

            val loadingAgain = awaitItem()
            assertEquals(ProductDetailUiState.Loading, loadingAgain)

            val success = awaitItem() as ProductDetailUiState.Success
            assertEquals(sampleProduct, success.product)
        }
    }
}
