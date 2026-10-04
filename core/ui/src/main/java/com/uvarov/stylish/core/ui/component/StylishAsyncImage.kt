package com.uvarov.stylish.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.uvarov.stylish.core.designsystem.theme.GraySurface

val DefaultLightPlaceholderColor: Color = GraySurface

@Composable
fun rememberLightPlaceholderPainter(
    color: Color = DefaultLightPlaceholderColor,
): Painter = remember(color) {
    ColorPainter(color)
}

@Composable
fun StylishAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    colorFilter: ColorFilter? = null,
    placeholder: Painter? = rememberLightPlaceholderPainter(),
    error: Painter? = placeholder,
) {
    if (LocalInspectionMode.current) {
        val previewPainter = if (model is Int) {
            painterResource(id = model)
        } else {
            placeholder ?: rememberLightPlaceholderPainter()
        }
        Image(
            painter = previewPainter,
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
            placeholder = placeholder,
            error = error,
            alignment = alignment,
            contentScale = contentScale,
            colorFilter = colorFilter,
        )
    }
}
