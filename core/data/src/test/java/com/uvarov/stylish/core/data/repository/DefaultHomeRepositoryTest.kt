package com.uvarov.stylish.core.data.repository

import app.cash.turbine.test
import com.uvarov.stylish.core.network.api.StylishApiService
import com.uvarov.stylish.core.network.model.BannerItemDto
import com.uvarov.stylish.core.network.model.CategoryDto
import com.uvarov.stylish.core.network.model.DealOfTheDayDto
import com.uvarov.stylish.core.network.model.HomeFeedResponseDto
import com.uvarov.stylish.core.network.model.ProductDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultHomeRepositoryTest {

    private val apiService: StylishApiService = mockk()
    private lateinit var repository: DefaultHomeRepository

    @Before
    fun setUp() {
        repository = DefaultHomeRepository(apiService)
    }

    @Test
    fun getHomeFeed_emitsMappedDomainHomeFeed() = runTest {
        val mockResponse = HomeFeedResponseDto(
            categories = listOf(
                CategoryDto("cat_1", "Beauty", "placeholder_cat_beauty"),
                CategoryDto("cat_gifts", "Gifts", "placeholder_cat_gifts")
            ),
            heroBanners = listOf(
                BannerItemDto("banner_1", "50% OFF", "All colors", "Shop Now", "placeholder_banner")
            ),
            dealOfTheDay = DealOfTheDayDto(
                title = "Deal of the Day",
                remainingTimeSeconds = 3600,
                products = listOf(
                    ProductDto(
                        id = "prod_1",
                        title = "Women Printed Kurta",
                        description = "Description",
                        currentPrice = 1500,
                        originalPrice = 2499,
                        discountPercent = 40,
                        rating = 4.0f,
                        reviewCount = 56890,
                        imageResName = "placeholder_kurta"
                    )
                )
            ),
            specialOfferBanner = BannerItemDto("special_1", "Special", "Subtitle"),
            heelsBanner = BannerItemDto("heels_1", "Heels", "Subtitle"),
            trendingProducts = emptyList(),
            newArrivalsBanner = BannerItemDto("new_1", "New Arrivals", "Subtitle"),
            sponsoredBanner = BannerItemDto("spon_1", "Sponsored", "Subtitle")
        )

        coEvery { apiService.getHomeFeed() } returns mockResponse

        repository.getHomeFeed().test {
            val feed = awaitItem()
            assertEquals(2, feed.categories.size)
            assertEquals("Beauty", feed.categories[0].name)
            assertEquals("Gifts", feed.categories[1].name)
            assertEquals("placeholder_cat_gifts", feed.categories[1].imageResName)
            assertEquals("Deal of the Day", feed.dealOfTheDay.title)
            assertEquals(1, feed.dealOfTheDay.products.size)
            assertEquals("Women Printed Kurta", feed.dealOfTheDay.products[0].title)
            assertEquals(1500, feed.dealOfTheDay.products[0].currentPrice)
            awaitComplete()
        }
    }
}
