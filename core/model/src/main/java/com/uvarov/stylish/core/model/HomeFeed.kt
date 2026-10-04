package com.uvarov.stylish.core.model

data class HomeFeed(
    val categories: List<Category>,
    val heroBanners: List<BannerItem>,
    val products: List<Product>,
)
