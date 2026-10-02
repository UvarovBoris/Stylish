package com.uvarov.stylish.core.data.repository

import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val isOnboardingCompleted: Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)
}
