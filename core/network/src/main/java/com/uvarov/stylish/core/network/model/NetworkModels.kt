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
    @SerialName("image_url") val imageUrl: String,
)

@Serializable
data class CategoryDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("image_url") val imageUrl: String,
)

@Serializable
data class BannerItemDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("subtitle") val subtitle: String,
    @SerialName("action_text") val actionText: String = "",
    @SerialName("image_url") val imageUrl: String = "",
)

@Serializable
data class HomeFeedResponseDto(
    @SerialName("categories") val categories: List<CategoryDto>,
    @SerialName("hero_banners") val heroBanners: List<BannerItemDto>,
    @SerialName("products") val products: List<ProductDto>,
)
