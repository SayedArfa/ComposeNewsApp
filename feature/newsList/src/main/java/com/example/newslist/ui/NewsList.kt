package com.example.newslist.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.core.common.ui.EndlessLazyColumn
import com.example.core.models.Article
import kotlinx.coroutines.launch

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
    EndlessLazyColumn(
        items = list,
        itemKey = { it.article.url ?: "" },
        itemContent = { NewsListItem(articleUiState = it, onItemClick, onFavoriteClick) },
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        loadMore()
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
                val favAnimate = remember {
                    Animatable(1f)
                }
                val scope = rememberCoroutineScope()
                IconButton(
                    onClick = {
                        onFavoriteClick(article)
                        scope.launch {
                            favAnimate.animateTo(
                                1f,
                                animationSpec = keyframes {
                                    durationMillis = 500
                                    1f at 0
                                    (if (articleUiState.isFavorite) 0.5f else 2f) at 250
                                    1f at 500
                                }
                            )
                        }

                    }, modifier = Modifier
                        .align(Alignment.End)
                        .padding(10.dp)
                        .scale(favAnimate.value)
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
