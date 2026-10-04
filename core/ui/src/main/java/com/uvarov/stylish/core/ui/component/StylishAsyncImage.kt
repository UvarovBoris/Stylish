package com.uvarov.stylish.core.ui.component

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
    model: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    colorFilter: ColorFilter? = null,
    placeholder: Painter? = rememberLightPlaceholderPainter(),
    error: Painter? = placeholder,
) {
    val request = ImageRequest.Builder(LocalContext.current)
        .data(model)
        .crossfade(true)
        .build()
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
