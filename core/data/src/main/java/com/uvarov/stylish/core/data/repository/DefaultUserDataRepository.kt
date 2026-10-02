package com.uvarov.stylish.core.data.repository

import com.uvarov.stylish.core.datastore.StylishPreferencesDataSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class DefaultUserDataRepository @Inject constructor(
    private val preferencesDataSource: StylishPreferencesDataSource,
) : UserDataRepository {

    override val isOnboardingCompleted: Flow<Boolean>
        get() = preferencesDataSource.isOnboardingCompleted

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        preferencesDataSource.setOnboardingCompleted(completed)
    }
}
