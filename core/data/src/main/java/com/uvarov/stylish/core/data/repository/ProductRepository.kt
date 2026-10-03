package com.uvarov.stylish.core.data.repository

import com.uvarov.stylish.core.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(category: String? = null, query: String? = null): Flow<List<Product>>
    fun getProductById(id: String): Flow<Product>
}
