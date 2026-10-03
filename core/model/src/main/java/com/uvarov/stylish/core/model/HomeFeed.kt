package com.uvarov.stylish.core.model

data class HomeFeed(
    val categories: List<Category>,
    val heroBanners: List<BannerItem>,
    val dealOfTheDay: DealOfTheDay,
    val specialOfferBanner: BannerItem,
    val heelsBanner: BannerItem,
    val trendingProducts: List<Product>,
    val newArrivalsBanner: BannerItem,
    val sponsoredBanner: BannerItem,
)
