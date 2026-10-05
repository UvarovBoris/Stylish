package com.uvarov.stylish.feature.productdetail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvarov.stylish.core.designsystem.theme.Montserrat
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.Product
import com.uvarov.stylish.core.ui.component.RatingBar
import com.uvarov.stylish.core.ui.component.StylishAsyncImage
import java.util.Locale
import com.uvarov.stylish.core.designsystem.R as DesignR

@Composable
fun ProductDetailScreen(
    productId: String,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(productId) {
        viewModel.onIntent(ProductDetailIntent.LoadProduct(productId))
    }

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ProductDetailSideEffect.NavigateBack -> onBackClick()
                is ProductDetailSideEffect.NavigateToCart -> onCartClick()
                is ProductDetailSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ProductDetailContent(
        uiState = uiState,
        modifier = modifier,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun ProductDetailContent(
    uiState: ProductDetailUiState,
    modifier: Modifier = Modifier,
    onIntent: (ProductDetailIntent) -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StylishTheme.colors.background,
        topBar = {
            ProductDetailTopBar(
                onBackClick = { onIntent(ProductDetailIntent.BackClicked) },
                onCartClick = { onIntent(ProductDetailIntent.CartClicked) },
                isFavorite = (uiState as? ProductDetailUiState.Success)?.product?.isFavorite ?: false,
                onFavoriteClick = { onIntent(ProductDetailIntent.ToggleFavorite) },
            )
        },
    ) { innerPadding ->
        when (uiState) {
            is ProductDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = StylishTheme.colors.brandPrimary)
                }
            }

            is ProductDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = uiState.message,
                            color = StylishTheme.colors.error,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Medium,
                        )
                        Button(
                            onClick = { onIntent(ProductDetailIntent.RetryClicked) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StylishTheme.colors.brandPrimary,
                            ),
                        ) {
                            Text(
                                text = stringResource(R.string.product_detail_retry),
                                color = StylishTheme.colors.onBrand,
                                fontFamily = Montserrat,
                            )
                        }
                    }
                }
            }

            is ProductDetailUiState.Success -> {
                ProductDetailLoadedContent(
                    state = uiState,
                    modifier = Modifier.padding(innerPadding),
                    onIntent = onIntent,
                )
            }
        }
    }
}

@Composable
private fun ProductDetailTopBar(
    onBackClick: () -> Unit,
    onCartClick: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(DesignR.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.product_detail_cd_back),
                tint = StylishTheme.colors.textPrimary,
                modifier = Modifier.size(24.dp),
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    painter = painterResource(
                        if (isFavorite) DesignR.drawable.ic_heart_filled
                        else DesignR.drawable.ic_heart_outline
                    ),
                    contentDescription = stringResource(
                        if (isFavorite) R.string.product_detail_cd_remove_from_wishlist
                        else R.string.product_detail_cd_add_to_wishlist
                    ),
                    tint = if (isFavorite) StylishTheme.colors.brandPrimary else StylishTheme.colors.textPrimary,
                )
            }
            IconButton(onClick = onCartClick) {
                Icon(
                    painter = painterResource(DesignR.drawable.ic_cart),
                    contentDescription = stringResource(R.string.product_detail_cd_cart),
                    tint = StylishTheme.colors.textPrimary,
                )
            }
        }
    }
}

@Composable
private fun ProductDetailLoadedContent(
    state: ProductDetailUiState.Success,
    modifier: Modifier = Modifier,
    onIntent: (ProductDetailIntent) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProductImageCarousel(imageUrl = state.product.imageUrl)

        SizeSelector(
            selectedSize = state.selectedSize,
            availableSizes = state.availableSizes,
            onSelectSize = { onIntent(ProductDetailIntent.SelectSize(it)) },
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = state.product.title,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 22.sp,
                color = StylishTheme.colors.textPrimary,
            )
            Text(
                text = state.product.description,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = StylishTheme.colors.textPrimary,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            RatingBar(
                rating = state.product.rating,
                starSize = 18.dp,
            )
            Text(
                text = String.format(Locale.US, "%,d", state.product.reviewCount),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = Color(0xFF828282),
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (state.product.originalPrice > state.product.currentPrice) {
                Text(
                    text = "₹${state.product.originalPrice}",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    color = Color(0xFF808488),
                    textDecoration = TextDecoration.LineThrough,
                )
            }
            Text(
                text = "₹${state.product.currentPrice}",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = StylishTheme.colors.textPrimary,
            )
            if (state.product.discountPercent > 0) {
                Text(
                    text = stringResource(R.string.product_detail_discount_format, state.product.discountPercent),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    color = StylishTheme.colors.brandSecondary,
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.product_detail_header),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                color = StylishTheme.colors.textPrimary,
            )
            Text(
                text = buildProductDetailDescription(state.product.description),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = StylishTheme.colors.textPrimary,
                maxLines = if (state.isDescriptionExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(
                    if (state.isDescriptionExpanded) R.string.product_detail_see_less else R.string.product_detail_see_more
                ),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = StylishTheme.colors.brandSecondary,
                modifier = Modifier.clickable { onIntent(ProductDetailIntent.ToggleDescription) },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FeatureChip(
                iconRes = R.drawable.ic_nearest_store,
                label = stringResource(R.string.product_detail_feature_nearest_store),
                modifier = Modifier.weight(1f),
            )
            FeatureChip(
                iconRes = R.drawable.ic_return_policy,
                label = stringResource(R.string.product_detail_feature_return_policy),
                modifier = Modifier.weight(1f),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = { onIntent(ProductDetailIntent.AddToCartClicked) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StylishTheme.colors.brandAccent,
                ),
            ) {
                Icon(
                    painter = painterResource(DesignR.drawable.ic_cart),
                    contentDescription = null,
                    tint = StylishTheme.colors.onBrand,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.product_detail_go_to_cart),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = StylishTheme.colors.onBrand,
                )
            }

            Button(
                onClick = { onIntent(ProductDetailIntent.BuyNowClicked) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StylishTheme.colors.success,
                ),
            ) {
                Text(
                    text = stringResource(R.string.product_detail_buy_now),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = StylishTheme.colors.onBrand,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xFFFFCCD5))
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Delivery in",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    color = StylishTheme.colors.textPrimary,
                )
                Text(
                    text = stringResource(R.string.product_detail_delivery_banner),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 21.sp,
                    color = StylishTheme.colors.textPrimary,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ProductImageCarousel(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    val pageCount = 4
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp)),
        ) {
            StylishAsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pageCount) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) {
                                StylishTheme.colors.carouselIndicatorActive
                            } else {
                                StylishTheme.colors.carouselIndicatorInactive
                            }
                        ),
                )
            }
        }
    }
}

@Composable
private fun SizeSelector(
    selectedSize: String,
    availableSizes: List<String>,
    onSelectSize: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.product_detail_size_label, selectedSize),
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 16.sp,
            color = StylishTheme.colors.textPrimary,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            availableSizes.forEach { size ->
                val isSelected = size == selectedSize
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (isSelected) StylishTheme.colors.brandPrimary else Color.Transparent
                        )
                        .border(
                            width = 1.dp,
                            color = StylishTheme.colors.brandPrimary,
                            shape = RoundedCornerShape(4.dp),
                        )
                        .clickable { onSelectSize(size) }
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = size,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        color = if (isSelected) StylishTheme.colors.onBrand else StylishTheme.colors.brandPrimary,
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureChip(
    iconRes: Int,
    label: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .border(
                width = 1.dp,
                color = StylishTheme.colors.textMuted.copy(alpha = 0.3f),
                shape = RoundedCornerShape(6.dp),
            )
            .padding(horizontal = 6.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = StylishTheme.colors.textSecondary,
            modifier = Modifier.size(14.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            color = StylishTheme.colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun buildProductDetailDescription(baseDescription: String): String {
    return if (baseDescription.length < 50) {
        "$baseDescription. Perhaps the most iconic style of all-time, crafted with premium materials and signature design cues that stand the test of time."
    } else {
        baseDescription
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailContentPreview() {
    StylishTheme {
        ProductDetailContent(
            uiState = ProductDetailUiState.Success(
                product = Product(
                    id = "prod_1",
                    title = "Nike Sneakers",
                    description = "Vision Alta Men's Shoes Size (All Colours)",
                    currentPrice = 1500,
                    originalPrice = 2999,
                    discountPercent = 50,
                    rating = 4.5f,
                    reviewCount = 56890,
                    imageUrl = "",
                ),
            ),
        )
    }
}
