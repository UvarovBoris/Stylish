package com.uvarov.stylish.core.data.repository

import com.uvarov.stylish.core.database.dao.FavoriteProductDao
import com.uvarov.stylish.core.database.entity.FavoriteProductEntity
import com.uvarov.stylish.core.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultWishlistRepository @Inject constructor(
    private val favoriteProductDao: FavoriteProductDao,
    private val productRepository: ProductRepository,
) : WishlistRepository {

    override fun getFavoriteProductIds(): Flow<Set<String>> {
        return favoriteProductDao.getFavoriteProductIds().map { it.toSet() }
    }

    override fun isFavorite(productId: String): Flow<Boolean> {
        return favoriteProductDao.isFavorite(productId)
    }

    override fun getFavoriteProducts(): Flow<List<Product>> {
        return combine(
            favoriteProductDao.getFavoriteProductIds(),
            productRepository.getProducts()
        ) { favoriteIds, allProducts ->
            val productMap = allProducts.associateBy { it.id }
            favoriteIds.mapNotNull { id ->
                productMap[id]?.copy(isFavorite = true)
            }
        }
    }

    override suspend fun toggleFavorite(productId: String) {
        val currentlyFavorite = favoriteProductDao.isFavorite(productId).first()
        if (currentlyFavorite) {
            favoriteProductDao.deleteFavorite(productId)
        } else {
            favoriteProductDao.insertFavorite(FavoriteProductEntity(productId = productId))
        }
    }

    override suspend fun addFavorite(productId: String) {
        favoriteProductDao.insertFavorite(FavoriteProductEntity(productId = productId))
    }

    override suspend fun removeFavorite(productId: String) {
        favoriteProductDao.deleteFavorite(productId)
    }
}
