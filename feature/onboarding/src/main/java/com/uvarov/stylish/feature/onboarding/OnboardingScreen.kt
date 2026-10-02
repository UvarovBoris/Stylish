package com.uvarov.stylish.feature.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uvarov.stylish.core.designsystem.theme.Montserrat
import com.uvarov.stylish.core.designsystem.theme.StylishTheme

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
    onComplete: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                OnboardingSideEffect.NavigateToAuth -> onComplete()
            }
        }
    }

    OnboardingContent(
        uiState = uiState,
        modifier = modifier,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun OnboardingContent(
    uiState: OnboardingUiState,
    modifier: Modifier = Modifier,
    onIntent: (OnboardingIntent) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = uiState.currentPage,
        pageCount = { uiState.totalPages }
    )

    // Sync Pager swipes with ViewModel state
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onIntent(OnboardingIntent.PageChanged(page))
        }
    }

    // Sync button clicks in ViewModel with Pager animation
    LaunchedEffect(uiState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            pagerState.animateScrollToPage(uiState.currentPage)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StylishTheme.colors.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        OnboardingTopBar(
            currentPage = uiState.currentPage,
            totalPages = uiState.totalPages,
            onSkipClick = { onIntent(OnboardingIntent.SkipClicked) }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            key = { index -> index }
        ) { pageIndex ->
            val page = uiState.pages[pageIndex]
            OnboardingPageContent(page = page)
        }

        OnboardingBottomBar(
            isFirstPage = uiState.isFirstPage,
            isLastPage = uiState.isLastPage,
            currentPage = uiState.currentPage,
            totalPages = uiState.totalPages,
            onPrevClick = { onIntent(OnboardingIntent.PrevClicked) },
            onNextClick = { onIntent(OnboardingIntent.NextClicked) },
            onGetStartedClick = { onIntent(OnboardingIntent.GetStartedClicked) }
        )
    }
}

@Composable
private fun OnboardingTopBar(
    currentPage: Int,
    totalPages: Int,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val indicatorText = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = StylishTheme.colors.textPrimary
                )
            ) {
                append("${currentPage + 1}")
            }
            withStyle(
                style = SpanStyle(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color(0xFFA0A0A1)
                )
            ) {
                append("/$totalPages")
            }
        }

        Text(
            text = indicatorText
        )

        Text(
            text = stringResource(R.string.onboarding_skip),
            style = StylishTheme.typography.onboardingText,
            color = StylishTheme.colors.textPrimary,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onSkipClick
            )
        )
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = stringResource(id = page.titleRes),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = page.titleRes),
            style = TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            ),
            color = StylishTheme.colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = page.descriptionRes),
            style = TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.25.sp
            ),
            color = Color(0xFFA8A8A9),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun OnboardingBottomBar(
    isFirstPage: Boolean,
    isLastPage: Boolean,
    currentPage: Int,
    totalPages: Int,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Prev button (hidden/disabled on page 0 to preserve spacing)
        Box(
            modifier = Modifier.size(width = 90.dp, height = 40.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (!isFirstPage) {
                Text(
                    text = stringResource(R.string.onboarding_prev),
                    style = StylishTheme.typography.onboardingText,
                    color = Color(0xFFC4C4C4),
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onPrevClick
                    )
                )
            }
        }

        // Animated Page Indicator (active = pill, inactive = dot)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (index in 0 until totalPages) {
                val isSelected = index == currentPage
                val targetWidth = if (isSelected) 40.dp else 10.dp
                val width by animateDpAsState(
                    targetValue = targetWidth,
                    animationSpec = tween(durationMillis = 300),
                    label = "indicator_width"
                )

                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .size(width = width, height = 10.dp)
                        .clip(if (isSelected) RoundedCornerShape(50) else CircleShape)
                        .background(
                            if (isSelected) {
                                Color(0xFF17223B)
                            } else {
                                Color(0x3317223B)
                            }
                        )
                )
            }
        }

        // Next / Get Started button
        Box(
            modifier = Modifier.size(width = 90.dp, height = 40.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (isLastPage) {
                Text(
                    text = stringResource(R.string.onboarding_start),
                    style = StylishTheme.typography.onboardingText,
                    color = StylishTheme.colors.brandPrimary,
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onGetStartedClick
                    )
                )
            } else {
                Text(
                    text = stringResource(R.string.onboarding_next),
                    style = StylishTheme.typography.onboardingText,
                    color = StylishTheme.colors.brandPrimary,
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onNextClick
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Page 1 - Choose Products")
@Composable
private fun OnboardingContentPage1Preview() {
    StylishTheme {
        OnboardingContent(
            uiState = OnboardingUiState(currentPage = 0),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Page 2 - Make Payment")
@Composable
private fun OnboardingContentPage2Preview() {
    StylishTheme {
        OnboardingContent(
            uiState = OnboardingUiState(currentPage = 1),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true, name = "Page 3 - Get Your Order")
@Composable
private fun OnboardingContentPage3Preview() {
    StylishTheme {
        OnboardingContent(
            uiState = OnboardingUiState(currentPage = 2),
            onIntent = {}
        )
    }
}
