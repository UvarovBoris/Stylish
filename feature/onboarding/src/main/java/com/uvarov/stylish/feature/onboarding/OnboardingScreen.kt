package com.uvarov.stylish.feature.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
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
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { uiState.totalPages }
    )

    // Handle system back gesture: navigate to previous page if not on the first page
    BackHandler(enabled = pagerState.currentPage > 0) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StylishTheme.colors.surface,
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
        topBar = {
            OnboardingTopBar(
                currentPage = pagerState.currentPage,
                totalPages = uiState.totalPages,
                onSkipClick = { onIntent(OnboardingIntent.SkipClicked) }
            )
        },
        bottomBar = {
            OnboardingBottomBar(
                currentPage = pagerState.currentPage,
                totalPages = uiState.totalPages,
                onPrevClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                onNextClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                onGetStartedClick = { onIntent(OnboardingIntent.StartClicked) }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            key = { index -> uiState.pages[index].titleRes }
        ) { pageIndex ->
            val page = uiState.pages[pageIndex]
            OnboardingPageContent(page = page)
        }
    }
}

@Composable
private fun OnboardingTopBar(
    currentPage: Int,
    totalPages: Int,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                append(stringResource(R.string.onboarding_page_counter_current, currentPage + 1))
            }
            withStyle(
                style = SpanStyle(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = StylishTheme.colors.textMuted
                )
            ) {
                append(stringResource(R.string.onboarding_page_counter_total, totalPages))
            }
        }

        Text(
            text = indicatorText
        )

        TextButton(
            onClick = onSkipClick,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.onboarding_skip),
                style = StylishTheme.typography.onboardingText,
                color = StylishTheme.colors.textPrimary
            )
        }
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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = page.titleRes),
            style = StylishTheme.typography.onboardingTitle,
            color = StylishTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics { heading() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = page.descriptionRes),
            style = StylishTheme.typography.onboardingDescription,
            color = StylishTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun OnboardingBottomBar(
    currentPage: Int,
    totalPages: Int,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFirstPage = currentPage == 0
    val isLastPage = currentPage == totalPages - 1

    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Prev button (hidden on first page to preserve spacing)
        Box(
            modifier = Modifier.size(width = 90.dp, height = 48.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (!isFirstPage) {
                TextButton(
                    onClick = onPrevClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_prev),
                        style = StylishTheme.typography.onboardingText,
                        color = StylishTheme.colors.textMuted
                    )
                }
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
                        .size(width = width, height = 10.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (isSelected) {
                                StylishTheme.colors.indicatorActive
                            } else {
                                StylishTheme.colors.indicatorInactive
                            }
                        )
                )
            }
        }

        // Next / Get Started button
        Box(
            modifier = Modifier.size(width = 90.dp, height = 48.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (isLastPage) {
                TextButton(
                    onClick = onGetStartedClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_start),
                        style = StylishTheme.typography.onboardingText,
                        color = StylishTheme.colors.brandPrimary
                    )
                }
            } else {
                TextButton(
                    onClick = onNextClick,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_next),
                        style = StylishTheme.typography.onboardingText,
                        color = StylishTheme.colors.brandPrimary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Onboarding Screen")
@Composable
private fun OnboardingContentPreview() {
    StylishTheme {
        OnboardingContent(
            uiState = OnboardingUiState(),
            onIntent = {}
        )
    }
}
