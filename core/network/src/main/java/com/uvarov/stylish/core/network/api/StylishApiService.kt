package com.uvarov.stylish.core.network.api

import com.uvarov.stylish.core.network.model.HomeFeedResponseDto
import com.uvarov.stylish.core.network.model.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StylishApiService {
    @GET("api/v1/home")
    suspend fun getHomeFeed(): HomeFeedResponseDto

    @GET("api/v1/products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("query") query: String? = null,
    ): List<ProductDto>

    @GET("api/v1/products")
    suspend fun getProductsByCategory(
        @Query("category") category: String,
    ): List<ProductDto>

    @GET("api/v1/products/{id}")
    suspend fun getProductById(
        @Path("id") id: String,
    ): ProductDto
}
