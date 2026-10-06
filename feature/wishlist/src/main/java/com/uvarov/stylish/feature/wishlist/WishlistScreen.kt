package com.uvarov.stylish.feature.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvarov.stylish.core.designsystem.theme.Montserrat
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.Product
import com.uvarov.stylish.core.model.ProductImportance
import com.uvarov.stylish.core.ui.component.ProductCard
import com.uvarov.stylish.core.designsystem.R as DesignR

@Composable
fun WishlistScreen(
    modifier: Modifier = Modifier,
    viewModel: WishlistViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is WishlistSideEffect.NavigateToProductDetails -> onProductClick(effect.productId)
            }
        }
    }

    WishlistContent(
        uiState = uiState,
        modifier = modifier,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun WishlistContent(
    uiState: WishlistUiState,
    modifier: Modifier = Modifier,
    onIntent: (WishlistIntent) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StylishTheme.colors.background),
    ) {
        WishlistTopBar(
            itemCount = (uiState as? WishlistUiState.Success)?.products?.size ?: 0,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            when (uiState) {
                is WishlistUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = StylishTheme.colors.brandPrimary)
                    }
                }

                is WishlistUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = uiState.message,
                            color = StylishTheme.colors.error,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onIntent(WishlistIntent.RetryClicked) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = StylishTheme.colors.brandPrimary,
                            ),
                        ) {
                            Text(
                                text = stringResource(R.string.wishlist_retry),
                                color = StylishTheme.colors.onBrand,
                                fontFamily = Montserrat,
                            )
                        }
                    }
                }

                is WishlistUiState.Success -> {
                    if (uiState.products.isEmpty()) {
                        WishlistEmptyState(modifier = Modifier.fillMaxSize())
                    } else {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 24.dp,
                            ),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalItemSpacing = 12.dp,
                        ) {
                            items(
                                items = uiState.products,
                                key = { it.id },
                            ) { product ->
                                ProductCard(
                                    product = product,
                                    isFavorite = true,
                                    onClick = { onIntent(WishlistIntent.ProductClicked(product.id)) },
                                    onFavoriteClick = { onIntent(WishlistIntent.ToggleFavorite(product.id)) },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WishlistTopBar(
    itemCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.wishlist_title),
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = StylishTheme.colors.textPrimary,
        )
        if (itemCount > 0) {
            Text(
                text = stringResource(R.string.wishlist_item_count, itemCount),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = StylishTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun WishlistEmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = StylishTheme.colors.brandContainer,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(DesignR.drawable.ic_heart_outline),
                contentDescription = null,
                tint = StylishTheme.colors.brandPrimary,
                modifier = Modifier.size(40.dp),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.wishlist_empty_title),
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = StylishTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.wishlist_empty_subtitle),
            fontFamily = Montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            color = StylishTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WishlistContentPreview() {
    StylishTheme {
        WishlistContent(
            uiState = WishlistUiState.Success(
                products = listOf(
                    Product(
                        id = "prod_1",
                        title = "Women Printed Kurta",
                        description = "Neque porro quisquam est qui dolorem ipsum quia",
                        currentPrice = 1500,
                        originalPrice = 2499,
                        discountPercent = 40,
                        rating = 4.5f,
                        reviewCount = 56890,
                        imageUrl = "https://api.stylish.app/images/kurta.jpg",
                        importance = ProductImportance.NORMAL,
                        isFavorite = true,
                    ),
                    Product(
                        id = "prod_2",
                        title = "HRX Sneakers",
                        description = "Neque porro quisquam est qui dolorem ipsum quia",
                        currentPrice = 2499,
                        originalPrice = 4999,
                        discountPercent = 50,
                        rating = 4.5f,
                        reviewCount = 344567,
                        imageUrl = "https://api.stylish.app/images/shoes.jpg",
                        importance = ProductImportance.HIGH,
                        isFavorite = true,
                    ),
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WishlistEmptyStatePreview() {
    StylishTheme {
        WishlistContent(
            uiState = WishlistUiState.Success(products = emptyList())
        )
    }
}
