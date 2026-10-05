package com.uvarov.stylish.core.data.repository

import com.uvarov.stylish.core.model.Product
import com.uvarov.stylish.core.network.api.StylishApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val apiService: StylishApiService,
) : ProductRepository {

    override fun getProducts(category: String?, query: String?): Flow<List<Product>> = flow {
        val dtos = apiService.getProducts(category = category, query = query)
        emit(dtos.map { it.asExternalModel() })
    }

    override fun getProductsByCategory(categoryId: String): Flow<List<Product>> = flow {
        val dtos = apiService.getProductsByCategory(categoryId)
        emit(dtos.map { it.asExternalModel() })
    }

    override fun getProductById(id: String): Flow<Product> = flow {
        val dto = apiService.getProductById(id)
        emit(dto.asExternalModel())
    }
}
