package com.uvarov.stylish.core.ui.util

import androidx.annotation.DrawableRes
import com.uvarov.stylish.core.designsystem.R

object ProductImageResolver {

    @DrawableRes
    fun resolveDrawable(imageResName: String): Int {
        return when (imageResName) {
            "placeholder_kurta" -> R.drawable.placeholder_kurta
            "placeholder_shoes" -> R.drawable.placeholder_shoes
            "placeholder_watch" -> R.drawable.placeholder_watch
            "placeholder_sneakers" -> R.drawable.placeholder_sneakers
            "placeholder_handbag" -> R.drawable.placeholder_handbag
            "placeholder_shirt" -> R.drawable.placeholder_shirt
            "placeholder_jacket" -> R.drawable.placeholder_jacket
            "placeholder_dress" -> R.drawable.placeholder_dress
            "placeholder_phone" -> R.drawable.placeholder_phone
            "placeholder_gadget" -> R.drawable.placeholder_gadget
            "placeholder_camera" -> R.drawable.placeholder_camera
            "placeholder_cat_beauty" -> R.drawable.placeholder_cat_beauty
            "placeholder_cat_fashion" -> R.drawable.placeholder_cat_fashion
            "placeholder_cat_kids" -> R.drawable.placeholder_cat_kids
            "placeholder_cat_mens" -> R.drawable.placeholder_cat_mens
            "placeholder_cat_womens" -> R.drawable.placeholder_cat_womens
            "placeholder_banner_hero" -> R.drawable.placeholder_banner_hero
            "placeholder_special_offer" -> R.drawable.placeholder_special_offer
            "placeholder_heels" -> R.drawable.placeholder_heels
            "placeholder_banner_summer" -> R.drawable.placeholder_banner_summer
            "placeholder_banner_sponsored" -> R.drawable.placeholder_banner_sponsored
            else -> R.drawable.placeholder_product
        }
    }
}
