package com.uvarov.stylish.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uvarov.stylish.core.designsystem.theme.CoralPink
import com.uvarov.stylish.core.designsystem.theme.Montserrat
import com.uvarov.stylish.core.designsystem.theme.StylishTheme
import com.uvarov.stylish.core.model.BannerItem
import com.uvarov.stylish.core.ui.util.ProductImageResolver

@Composable
fun HomeHeroBanner(
    banner: BannerItem,
    onBannerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
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

        Spacer(modifier = Modifier.height(8.dp))

        // Indicator dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(CoralPink, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(StylishTheme.colors.textMuted.copy(alpha = 0.4f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(StylishTheme.colors.textMuted.copy(alpha = 0.4f), CircleShape)
            )
        }
    }
}
