package com.example.newslist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.core.Result
import com.example.core.models.Article
import com.example.core.models.NewsResponse
import kotlinx.coroutines.flow.Flow

@Composable
internal fun NewsListScreen(
    onItemClick: (Article) -> Unit,
    newsFlow: Flow<Result<NewsResponse>>
) {
    when (val result = newsFlow.collectAsState(Result.Loading).value) {
        is Result.Error -> {}
        Result.Loading -> {}
        is Result.Success -> NewsList(result.data.articles, onItemClick)
    }

}

@Composable
internal fun NewsList(list: List<Article>, onItemClick: (Article) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(5.dp)
    ) {
        items(list) {
            NewsListItem(article = it, onItemClick)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun NewsListItem(
    article: Article, onItemClick: (Article) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .shadow(elevation = 5.dp, shape = MaterialTheme.shapes.medium)

    ) {
        Row(modifier = Modifier
            .padding(10.dp)
            .clickable {
                onItemClick(article)
            }) {
            GlideImage(
                model = article.urlToImage,
                null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.small)
            )
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "${article.title}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "${article.author}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
