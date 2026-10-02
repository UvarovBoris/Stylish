package com.uvarov.stylish.feature.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class MainTab(
    @get:StringRes val labelRes: Int,
    @get:DrawableRes val iconRes: Int,
) {
    HOME(
        labelRes = R.string.main_tab_home,
        iconRes = R.drawable.ic_nav_home,
    ),
    WISHLIST(
        labelRes = R.string.main_tab_wishlist,
        iconRes = R.drawable.ic_nav_wishlist,
    ),
    CART(
        labelRes = R.string.main_tab_cart,
        iconRes = R.drawable.ic_nav_cart,
    ),
    SEARCH(
        labelRes = R.string.main_tab_search,
        iconRes = R.drawable.ic_nav_search,
    ),
    SETTINGS(
        labelRes = R.string.main_tab_settings,
        iconRes = R.drawable.ic_nav_settings,
    ),
}
