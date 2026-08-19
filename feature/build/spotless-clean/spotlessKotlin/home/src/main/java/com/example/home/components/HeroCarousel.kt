package com.example.home.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.components.DynamicAsyncImage
import com.example.model.MovieItem
import kotlinx.coroutines.delay

@Composable
fun HeroCarousel(movies: List<MovieItem>, modifier: Modifier = Modifier, onBannerClick: (movie: MovieItem) -> Unit = {}) {
    if (movies.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { movies.size })

    LaunchedEffect(movies.size) {
        if (movies.size > 1) {
            while (true) {
                delay(3000) // Time visible per slide

                val nextPage = (pagerState.currentPage + 1) % movies.size
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing), // Smooth transition curve
                )
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        // Hero Image Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val movie = movies[page]

            // 2. Centered Image layout
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onBannerClick(movie) },
                contentAlignment = Alignment.Center,
            ) {
                DynamicAsyncImage(
                    imageUrl = movie.posterPath ?: "",
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    text = movie.title,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 30.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        // 3. Limited to 3 Indicator Dots
        val maxDots = movies.size
        val dotCount = minOf(maxDots, movies.size)
        val activeDotIndex = pagerState.currentPage % dotCount

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(dotCount) { iteration ->
                val isSelected = iteration == activeDotIndex
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
                        ),
                )
            }
        }
    }
}
