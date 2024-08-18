package com.example.newdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
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
        Row {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "${article?.title}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.clickable {
                    onBack()
                }
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
        GlideImage(
            model = article?.urlToImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Green),
            contentScale = ContentScale.FillWidth,
        )
        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = "${article?.description}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}