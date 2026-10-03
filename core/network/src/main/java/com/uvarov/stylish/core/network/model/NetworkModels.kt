package com.uvarov.stylish.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String = "",
    @SerialName("current_price") val currentPrice: Int,
    @SerialName("original_price") val originalPrice: Int,
    @SerialName("discount_percent") val discountPercent: Int,
    @SerialName("rating") val rating: Float,
    @SerialName("review_count") val reviewCount: Int,
    @SerialName("image_res_name") val imageResName: String,
)

@Serializable
data class CategoryDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("image_res_name") val imageResName: String,
)

@Serializable
data class BannerItemDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("subtitle") val subtitle: String,
    @SerialName("action_text") val actionText: String = "",
    @SerialName("image_res_name") val imageResName: String = "",
)

@Serializable
data class DealOfTheDayDto(
    @SerialName("title") val title: String,
    @SerialName("remaining_time_seconds") val remainingTimeSeconds: Long,
    @SerialName("products") val products: List<ProductDto>,
)

@Serializable
data class HomeFeedResponseDto(
    @SerialName("categories") val categories: List<CategoryDto>,
    @SerialName("hero_banners") val heroBanners: List<BannerItemDto>,
    @SerialName("deal_of_the_day") val dealOfTheDay: DealOfTheDayDto,
    @SerialName("special_offer_banner") val specialOfferBanner: BannerItemDto,
    @SerialName("heels_banner") val heelsBanner: BannerItemDto,
    @SerialName("trending_products") val trendingProducts: List<ProductDto>,
    @SerialName("new_arrivals_banner") val newArrivalsBanner: BannerItemDto,
    @SerialName("sponsored_banner") val sponsoredBanner: BannerItemDto,
)
