package com.uvarov.stylish.feature.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvarov.stylish.core.designsystem.theme.CoralPink
import com.uvarov.stylish.core.designsystem.theme.Montserrat
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.BannerItem
import com.uvarov.stylish.core.ui.util.ProductImageResolver
import kotlinx.coroutines.launch

@Composable
fun HomeHeroBanner(
    banners: List<BannerItem>,
    onBannerClick: (BannerItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (banners.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { banners.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 16.dp,
            key = { index -> banners[index].id }
        ) { page ->
            val banner = banners[page]
            HeroBannerCard(
                banner = banner,
                onBannerClick = { onBannerClick(banner) }
            )
        }

        if (banners.size > 1) {
            Spacer(modifier = Modifier.height(8.dp))

            // Indicator dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(banners.size) { index ->
                    val isSelected = index == pagerState.currentPage
                    val dotColor by animateColorAsState(
                        targetValue = if (isSelected) CoralPink else StylishTheme.colors.textMuted.copy(alpha = 0.4f),
                        animationSpec = tween(durationMillis = 300),
                        label = "hero_dot_color"
                    )

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(dotColor, CircleShape)
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroBannerCard(
    banner: BannerItem,
    onBannerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onBannerClick),
        shape = RoundedCornerShape(12.dp),
        color = CoralPink,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = banner.title,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = banner.subtitle,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.White
                )

                if (banner.actionText.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Transparent,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                        modifier = Modifier.clickable(onClick = onBannerClick)
                    ) {
                        Text(
                            text = "${banner.actionText} →",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            val drawableRes = ProductImageResolver.resolveDrawable(banner.imageResName)
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = banner.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Preview(showBackground = true, name = "Multiple Hero Banners Carousel")
@Composable
private fun HomeHeroBannerCarouselPreview() {
    StylishTheme {
        HomeHeroBanner(
            banners = listOf(
                BannerItem(
                    id = "1",
                    title = "50-40% OFF",
                    subtitle = "Now in (product)\nAll colours",
                    actionText = "Shop Now",
                    imageResName = "placeholder_banner_hero"
                ),
                BannerItem(
                    id = "2",
                    title = "Summer Wave",
                    subtitle = "Top picks on fresh styles\nLimited time",
                    actionText = "Shop Now",
                    imageResName = "placeholder_banner_hero"
                ),
                BannerItem(
                    id = "3",
                    title = "Exclusive Deals",
                    subtitle = "Up to 70% off trending styles\nCheck it out",
                    actionText = "Shop Now",
                    imageResName = "placeholder_banner_hero"
                )
            ),
            onBannerClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Single Hero Banner")
@Composable
private fun HomeHeroBannerSinglePreview() {
    StylishTheme {
        HomeHeroBanner(
            banners = listOf(
                BannerItem(
                    id = "1",
                    title = "50-40% OFF",
                    subtitle = "Now in (product)\nAll colours",
                    actionText = "Shop Now",
                    imageResName = "placeholder_banner_hero"
                )
            ),
            onBannerClick = {}
        )
    }
}
