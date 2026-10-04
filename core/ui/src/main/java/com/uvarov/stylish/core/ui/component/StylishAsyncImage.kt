package com.uvarov.stylish.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.uvarov.stylish.core.designsystem.R

@Composable
fun StylishAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    colorFilter: ColorFilter? = null,
    @DrawableRes placeholderRes: Int = R.drawable.placeholder_product,
    @DrawableRes errorRes: Int = R.drawable.placeholder_product,
) {
    if (LocalInspectionMode.current) {
        val previewRes = when (model) {
            is Int -> model
            else -> placeholderRes
        }
        Image(
            painter = painterResource(id = previewRes),
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale,
            colorFilter = colorFilter,
        )
    } else {
        val context = LocalContext.current
        val request = if (model is ImageRequest) {
            model
        } else {
            ImageRequest.Builder(context)
                .data(model)
                .crossfade(true)
                .build()
        }

        AsyncImage(
            model = request,
            contentDescription = contentDescription,
            modifier = modifier,
            placeholder = painterResource(id = placeholderRes),
            error = painterResource(id = errorRes),
            alignment = alignment,
            contentScale = contentScale,
            colorFilter = colorFilter,
        )
    }
}
