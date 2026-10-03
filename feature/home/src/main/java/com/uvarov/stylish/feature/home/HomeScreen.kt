package com.uvarov.stylish.feature.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvarov.stylish.core.designsystem.R
import com.uvarov.stylish.core.designsystem.theme.BlueAccent
import com.uvarov.stylish.core.designsystem.theme.CoralPink
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.BannerItem
import com.uvarov.stylish.core.model.Category
import com.uvarov.stylish.core.model.DealOfTheDay
import com.uvarov.stylish.core.model.HomeFeed
import com.uvarov.stylish.core.model.Product
import com.uvarov.stylish.core.ui.component.ProductCard
import com.uvarov.stylish.feature.home.components.HeelsBanner
import com.uvarov.stylish.feature.home.components.HomeCategoriesRow
import com.uvarov.stylish.feature.home.components.HomeFeaturedHeader
import com.uvarov.stylish.feature.home.components.HomeHeroBanner
import com.uvarov.stylish.feature.home.components.HomeSearchBar
import com.uvarov.stylish.feature.home.components.HomeSectionHeader
import com.uvarov.stylish.feature.home.components.HomeTopBar
import com.uvarov.stylish.feature.home.components.SpecialOffersBanner
import com.uvarov.stylish.feature.home.components.SponsoredBanner
import com.uvarov.stylish.feature.home.components.SummerSaleBanner
import com.uvarov.stylish.feature.home.R as HomeR

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit = {},
    onNavigateToCatalog: (categoryId: String?, title: String?) -> Unit = { _, _ -> },
    onNavigateToSearch: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HomeSideEffect.NavigateToProductDetails -> onProductClick(effect.productId)
                is HomeSideEffect.NavigateToCatalog -> onNavigateToCatalog(effect.categoryId, effect.title)
                is HomeSideEffect.NavigateToSearch -> onNavigateToSearch()
                is HomeSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    HomeContent(
        uiState = uiState,
        modifier = modifier,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onIntent: (HomeIntent) -> Unit,
) {
    when (uiState) {
        is HomeUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = StylishTheme.colors.brandPrimary)
            }
        }

        is HomeUiState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.message,
                        style = StylishTheme.typography.sectionTitle,
                        color = StylishTheme.colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onIntent(HomeIntent.LoadHomeFeed) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StylishTheme.colors.brandPrimary
                        )
                    ) {
                        Text(text = stringResource(id = HomeR.string.retry))
                    }
                }
            }
        }

        is HomeUiState.Success -> {
            val feed = uiState.homeFeed

            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                // Top App Bar
                item {
                    HomeTopBar(
                        onAvatarClick = { /* Profile action */ }
                    )
                }

                // Search Bar
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HomeSearchBar(
                        onSearchClick = { onIntent(HomeIntent.SearchBarClicked) },
                        onVoiceClick = { onIntent(HomeIntent.VoiceSearchClicked) }
                    )
                }

                // "All Featured" Header
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HomeFeaturedHeader()
                }

                // Categories Row
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HomeCategoriesRow(
                        categories = feed.categories,
                        onCategoryClick = { category ->
                            onIntent(HomeIntent.CategoryClicked(categoryId = category.id, title = category.name))
                        }
                    )
                }

                // Hero Banner
                if (feed.heroBanners.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        HomeHeroBanner(
                            banner = feed.heroBanners.first(),
                            onBannerClick = { onIntent(HomeIntent.SpecialOffersClicked) }
                        )
                    }
                }

                // Deal of the Day Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HomeSectionHeader(
                        title = feed.dealOfTheDay.title,
                        subtitle = "22h 55m 20s ${stringResource(id = HomeR.string.remaining)}",
                        subtitleIconRes = R.drawable.ic_clock,
                        backgroundColor = BlueAccent,
                        onViewAllClick = { onIntent(HomeIntent.DealOfTheDayViewAllClicked) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = feed.dealOfTheDay.products,
                            key = { it.id }
                        ) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onIntent(HomeIntent.ProductClicked(product)) }
                            )
                        }
                    }
                }

                // Special Offers Callout
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SpecialOffersBanner(
                        banner = feed.specialOfferBanner,
                        onClick = { onIntent(HomeIntent.SpecialOffersClicked) }
                    )
                }

                // Flat and Heels Banner
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HeelsBanner(
                        banner = feed.heelsBanner,
                        onClick = { onIntent(HomeIntent.HeelsBannerClicked) }
                    )
                }

                // Trending Products Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HomeSectionHeader(
                        title = stringResource(id = HomeR.string.trending_products),
                        subtitle = "Last Date 29/02/22",
                        subtitleIconRes = R.drawable.ic_calendar,
                        backgroundColor = CoralPink,
                        onViewAllClick = { onIntent(HomeIntent.TrendingViewAllClicked) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = feed.trendingProducts,
                            key = { it.id }
                        ) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onIntent(HomeIntent.ProductClicked(product)) }
                            )
                        }
                    }
                }

                // New Arrivals / Summer Sale Banner
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SummerSaleBanner(
                        banner = feed.newArrivalsBanner,
                        onClick = { onIntent(HomeIntent.NewArrivalsClicked) }
                    )
                }

                // Sponsored Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SponsoredBanner(
                        banner = feed.sponsoredBanner,
                        onClick = { onIntent(HomeIntent.SponsoredBannerClicked) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    val sampleProducts = listOf(
        Product(
            id = "prod_1",
            title = "Women Printed Kurta",
            description = "Neque porro quisquam est qui dolorem ipsum quia",
            currentPrice = 1500,
            originalPrice = 2499,
            discountPercent = 40,
            rating = 4.0f,
            reviewCount = 56890,
            imageResName = "placeholder_kurta"
        ),
        Product(
            id = "prod_2",
            title = "HRX by Hrithik Roshan",
            description = "Neque porro quisquam est qui dolorem ipsum quia",
            currentPrice = 2499,
            originalPrice = 4999,
            discountPercent = 50,
            rating = 4.5f,
            reviewCount = 344567,
            imageResName = "placeholder_shoes"
        )
    )

    val sampleCategories = listOf(
        Category("1", "Beauty", "placeholder_cat_beauty"),
        Category("2", "Fashion", "placeholder_cat_fashion"),
        Category("3", "Kids", "placeholder_cat_kids"),
        Category("4", "Mens", "placeholder_cat_mens"),
        Category("5", "Womens", "placeholder_cat_womens"),
        Category("6", "Gifts", "placeholder_cat_gifts")
    )

    val sampleFeed = HomeFeed(
        categories = sampleCategories,
        heroBanners = listOf(
            BannerItem("1", "50-40% OFF", "Now in (product)\nAll colours", "Shop Now", "placeholder_banner_hero")
        ),
        dealOfTheDay = DealOfTheDay("Deal of the Day", 82000, sampleProducts),
        specialOfferBanner = BannerItem("2", "Special Offers", "We make sure you get the offer you need at best prices", "", "placeholder_special_offer"),
        heelsBanner = BannerItem("3", "Flat and Heels", "Stand a chance to get rewarded", "Visit now", "placeholder_heels"),
        trendingProducts = sampleProducts,
        newArrivalsBanner = BannerItem("4", "New Arrivals", "Summer' 25 Collections", "View all", "placeholder_banner_summer"),
        sponsoredBanner = BannerItem("5", "Sponserd", "up to 50% Off", "UP TO 50% OFF", "placeholder_banner_sponsored")
    )

    StylishTheme {
        HomeContent(
            uiState = HomeUiState.Success(homeFeed = sampleFeed),
            onIntent = {}
        )
    }
}
