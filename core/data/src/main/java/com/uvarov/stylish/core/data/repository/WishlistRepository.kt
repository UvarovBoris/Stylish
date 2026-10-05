package com.uvarov.stylish.core.data.repository

import com.uvarov.stylish.core.model.Product
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getFavoriteProductIds(): Flow<Set<String>>
    fun isFavorite(productId: String): Flow<Boolean>
    fun getFavoriteProducts(): Flow<List<Product>>
    suspend fun toggleFavorite(productId: String)
    suspend fun addFavorite(productId: String)
    suspend fun removeFavorite(productId: String)
}
