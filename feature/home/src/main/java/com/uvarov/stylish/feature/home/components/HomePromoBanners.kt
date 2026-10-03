package com.uvarov.stylish.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvarov.stylish.core.designsystem.R
import com.uvarov.stylish.core.designsystem.theme.CoralRed
import com.uvarov.stylish.core.designsystem.theme.Montserrat
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.BannerItem
import com.uvarov.stylish.core.ui.util.ProductImageResolver
import com.uvarov.stylish.feature.home.R as HomeR

@Composable
fun SpecialOffersBanner(
    banner: BannerItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = StylishTheme.colors.surface,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val drawableRes = ProductImageResolver.resolveDrawable(banner.imageResName)
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = banner.title,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = StylishTheme.colors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "😱", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = banner.subtitle,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = StylishTheme.colors.textSecondary
                )
            }
        }
    }
}

@Composable
fun HeelsBanner(
    banner: BannerItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFFF8E7),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val drawableRes = ProductImageResolver.resolveDrawable(banner.imageResName)
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = banner.title,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = StylishTheme.colors.textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = banner.subtitle,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = StylishTheme.colors.textSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = CoralRed,
                    modifier = Modifier.clickable(onClick = onClick)
                ) {
                    Text(
                        text = "${stringResource(id = HomeR.string.visit_now)} →",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SummerSaleBanner(
    banner: BannerItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = StylishTheme.colors.surface,
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val drawableRes = ProductImageResolver.resolveDrawable(banner.imageResName)
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = banner.title,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = StylishTheme.colors.textPrimary
                    )
                    Text(
                        text = banner.subtitle,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = StylishTheme.colors.textSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = CoralRed,
                    modifier = Modifier.clickable(onClick = onClick)
                ) {
                    Text(
                        text = "${stringResource(id = HomeR.string.view_all)} →",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SponsoredBanner(
    banner: BannerItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(id = HomeR.string.sponsored),
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = StylishTheme.colors.textPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(8.dp),
            color = StylishTheme.colors.surface,
            shadowElevation = 2.dp,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                val drawableRes = ProductImageResolver.resolveDrawable(banner.imageResName)
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = banner.subtitle,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = StylishTheme.colors.textPrimary
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_right),
                        contentDescription = null,
                        tint = StylishTheme.colors.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
