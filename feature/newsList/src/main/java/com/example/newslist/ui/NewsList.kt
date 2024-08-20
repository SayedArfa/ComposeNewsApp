package com.example.newslist.ui

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.core.models.Article

@Composable
internal fun NewsListScreen(
    newsUiState: NewsUiState,
    onItemClick: (Article) -> Unit,
    onFavoriteClick: (Article) -> Unit,
    onRetry: () -> Unit,
    loadMore: () -> Unit,
    onShowSnackBar: suspend (String, String?, () -> Unit) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        val showSnackBar = remember {
            mutableIntStateOf(0)
        }
        NewsList(
            newsUiState.articles,
            loadMore,
            onItemClick,
            onFavoriteClick,
            modifier = Modifier.weight(1f, true)
        )
        if (newsUiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(10.dp)
                    .animateContentSize()
            )
        }
        newsUiState.error?.let {
            showSnackBar.intValue++
            LaunchedEffect(showSnackBar) {
                onShowSnackBar(it.errorType.toString(), "Retry") {
                    onRetry()
                }
            }
        }

    }
}

@Composable
internal fun NewsList(
    list: List<ArticleUiState>,
    loadMore: () -> Unit,
    onItemClick: (Article) -> Unit,
    onFavoriteClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val buffer = 1 // load more when scroll reaches last n item, where n >= 1
    val listState = rememberLazyListState()

    // observe list scrolling
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - buffer
        }
    }

    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) loadMore()
    }
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(5.dp),
        modifier = modifier
    ) {
        items(list) {
            key(it.article.id) {
                NewsListItem(articleUiState = it, onItemClick, onFavoriteClick)
            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun NewsListItem(
    articleUiState: ArticleUiState,
    onItemClick: (Article) -> Unit,
    onFavoriteClick: (Article) -> Unit
) {
    val article = articleUiState.article
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
                IconButton(
                    onClick = { onFavoriteClick(article) }, modifier = Modifier
                        .align(Alignment.End)
                        .padding(10.dp)
                ) {
                    Icon(
                        if (articleUiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null
                    )
                }
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
