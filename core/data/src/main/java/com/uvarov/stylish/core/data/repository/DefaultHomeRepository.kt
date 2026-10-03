package com.uvarov.stylish.core.data.repository

import com.uvarov.stylish.core.model.BannerItem
import com.uvarov.stylish.core.model.Category
import com.uvarov.stylish.core.model.DealOfTheDay
import com.uvarov.stylish.core.model.HomeFeed
import com.uvarov.stylish.core.model.Product
import com.uvarov.stylish.core.network.api.StylishApiService
import com.uvarov.stylish.core.network.model.BannerItemDto
import com.uvarov.stylish.core.network.model.CategoryDto
import com.uvarov.stylish.core.network.model.DealOfTheDayDto
import com.uvarov.stylish.core.network.model.HomeFeedResponseDto
import com.uvarov.stylish.core.network.model.ProductDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultHomeRepository @Inject constructor(
    private val apiService: StylishApiService,
) : HomeRepository {

    override fun getHomeFeed(): Flow<HomeFeed> = flow {
        val response = apiService.getHomeFeed()
        emit(response.asExternalModel())
    }
}

fun ProductDto.asExternalModel(): Product = Product(
    id = id,
    title = title,
    description = description,
    currentPrice = currentPrice,
    originalPrice = originalPrice,
    discountPercent = discountPercent,
    rating = rating,
    reviewCount = reviewCount,
    imageResName = imageResName,
)

fun CategoryDto.asExternalModel(): Category = Category(
    id = id,
    name = name,
    imageResName = imageResName,
)

fun BannerItemDto.asExternalModel(): BannerItem = BannerItem(
    id = id,
    title = title,
    subtitle = subtitle,
    actionText = actionText,
    imageResName = imageResName,
)

fun DealOfTheDayDto.asExternalModel(): DealOfTheDay = DealOfTheDay(
    title = title,
    remainingTimeSeconds = remainingTimeSeconds,
    products = products.map { it.asExternalModel() },
)

fun HomeFeedResponseDto.asExternalModel(): HomeFeed = HomeFeed(
    categories = categories.map { it.asExternalModel() },
    heroBanners = heroBanners.map { it.asExternalModel() },
    dealOfTheDay = dealOfTheDay.asExternalModel(),
    specialOfferBanner = specialOfferBanner.asExternalModel(),
    heelsBanner = heelsBanner.asExternalModel(),
    trendingProducts = trendingProducts.map { it.asExternalModel() },
    newArrivalsBanner = newArrivalsBanner.asExternalModel(),
    sponsoredBanner = sponsoredBanner.asExternalModel(),
)
