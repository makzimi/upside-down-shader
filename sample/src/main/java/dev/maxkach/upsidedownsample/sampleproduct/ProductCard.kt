package dev.maxkach.upsidedownsample.sampleproduct

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import dev.maxkach.upsidedownsample.R
import kotlin.math.absoluteValue

@Composable
fun ProductCard(
    state: ProductCardState,
    stepTitle: String,
    onColorClicked: (Int) -> Unit,
    onImageChanged: (Int) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        topBar = { TopBar(stepTitle, onBackPressed) },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(
//                        top = innerPadding.calculateTopPadding() + 12.dp,
//                        bottom = innerPadding.calculateBottomPadding() + 12.dp,
                    ),
            ) {
                Images(
                    images = state.images,
                    modifier = Modifier,
                    onImageChanged = onImageChanged
                )

                AboutProduct(
                    description = state.description,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 20.dp)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    title: String,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        title = {
            Text(
                text = title,
                modifier = modifier,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Composable
private fun AboutProduct(
    description: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Images(
    images: ProductCardState.ImagesState,
    modifier: Modifier = Modifier,
    onImageChanged: (Int) -> Unit = { },
) {
    val pagerState = rememberPagerState(
        initialPage = images.currentImage,
        pageCount = { images.imagesRes.size }
    )

    LaunchedEffect(images.currentImage) {
        if (pagerState.currentPage != images.currentImage) {
            pagerState.animateScrollToPage(images.currentImage)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page != images.currentImage) {
                onImageChanged(page)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
//            contentPadding = PaddingValues(horizontal = 40.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
//            val scale = lerp(
//                start = 0.7f,
//                stop = 1f,
//                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
//            )

//            val translationX = pageOffset * 100f

            Image(
                painter = painterResource(images.imagesRes[page].imageRes),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(9/16f)
                    .fillMaxWidth()
                    .graphicsLayer {
//                        scaleX = scale
//                        scaleY = scale
//                        this.translationX = translationX
                    }
//                    .clip(shape = RoundedCornerShape(20.dp))
            )
        }
    }
}
