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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.core.common.ui.TitleBar
import com.example.core.models.Article

@Composable
fun FavoriteRoute(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onItemClick: (Article) -> Unit
) {
    Column {
        TitleBar(title = "Favorites") {
        }
        Spacer(modifier = Modifier.size(10.dp))
        FavoritesList(
            viewModel.favListFlow.collectAsState(initial = listOf()).value,
            onItemClick
        ) { viewModel.removeArticle(it) }
    }
}

@Composable
fun FavoritesList(
    articles: List<Article>,
    onItemClick: (Article) -> Unit,
    onRemove: (Article) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 50.dp),
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize()
    ) {
        items(items = articles) { item: Article ->
            FavoriteItem(Modifier.animateItem(), article = item, onItemClick, onRemove)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavoriteItem(
    modifier: Modifier,
    article: Article, onItemClick: (Article) -> Unit,
    onRemove: (Article) -> Unit
) {
    Card(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            )
            .shadow(elevation = 5.dp, shape = MaterialTheme.shapes.medium)

    ) {
        Row(
            modifier = Modifier
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
                    onClick = {
                        onRemove(article)
                    }, modifier = Modifier
                        .align(Alignment.End)
                        .padding(10.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
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