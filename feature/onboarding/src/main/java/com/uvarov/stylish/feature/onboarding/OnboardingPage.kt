package com.uvarov.stylish.feature.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class OnboardingPage(
    @param:DrawableRes val imageRes: Int,
    @param:StringRes val titleRes: Int,
    @param:StringRes val descriptionRes: Int
)

val defaultOnboardingPages = listOf(
    OnboardingPage(
        imageRes = R.drawable.onboarding_choose_products,
        titleRes = R.string.onboarding_title_1,
        descriptionRes = R.string.onboarding_description_1
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding_make_payment,
        titleRes = R.string.onboarding_title_2,
        descriptionRes = R.string.onboarding_description_2
    ),
    OnboardingPage(
        imageRes = R.drawable.onboarding_get_your_order,
        titleRes = R.string.onboarding_title_3,
        descriptionRes = R.string.onboarding_description_3
    )
)
