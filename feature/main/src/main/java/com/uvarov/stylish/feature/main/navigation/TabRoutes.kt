package com.uvarov.stylish.feature.main.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object WishlistRoute : NavKey

@Serializable
data object CartRoute : NavKey

@Serializable
data object SearchRoute : NavKey

@Serializable
data object SettingsRoute : NavKey
