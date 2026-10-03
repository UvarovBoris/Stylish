package com.uvarov.stylish.core.data.repository

import com.uvarov.stylish.core.model.HomeFeed
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeFeed(): Flow<HomeFeed>
}
