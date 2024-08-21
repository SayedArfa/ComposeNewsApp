package com.example.newdetails

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.core.common.ui.TitleBar
import com.example.core.models.Article

@Composable
fun ArticleDetailsRoute(article: Article?, onBack: () -> Unit) {
    ArticleDetailsScreen(article, onBack)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun ArticleDetailsScreen(article: Article?, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val scale = remember {
            Animatable(0.5f)
        }
        val alpha = remember {
            Animatable(0f)
        }
        LaunchedEffect(key1 = Unit) {
            scale.animateTo(1f, animationSpec = tween(durationMillis = 1000))
        }
        LaunchedEffect(key1 = Unit) {
            alpha.animateTo(1f, animationSpec = tween(durationMillis = 1000, delayMillis = 500))
        }
        TitleBar(title = article?.title ?: "", modifier = Modifier.scale(scale.value)) {
            onBack()
        }
        Spacer(modifier = Modifier.size(10.dp))
        GlideImage(
            model = article?.urlToImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = "${article?.description}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alpha(alpha.value)
        )
    }
}