package com.example.newslist.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.core.Result
import com.example.core.models.Article

@Composable
internal fun NewsListScreen(
    onItemClick: (Article) -> Unit,
    viewModel: NewsViewModel = hiltViewModel()
) {
    when (val result = viewModel.breakingNews.collectAsState().value) {
        is Result.Error -> {}
        Result.Loading -> {}
        is Result.Success -> NewsList(result.data.articles, onItemClick)
    }

}

@Composable
internal fun NewsList(list: List<Article>, onItemClick: (Article) -> Unit) {
    LazyColumn {
        items(list) {
            NewsListItem(article = it, onItemClick)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun NewsListItem(
    article: Article, onItemClick: (Article) -> Unit, modifier: Modifier = Modifier
) {
    Row(modifier = Modifier
        .padding(10.dp)
        .clickable {
            onItemClick(article)
        }) {
        GlideImage(
            model = "https://www.kasandbox.org/programming-images/avatars/purple-pi.png",
            null,
            modifier = Modifier
                .size(100.dp)
                .fillMaxSize()
        )
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "${article.title}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = "${article.author}"
            )
        }
    }
}
