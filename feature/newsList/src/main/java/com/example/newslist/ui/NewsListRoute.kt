package com.example.newslist.ui

import androidx.compose.runtime.Composable
import com.example.core.models.Article

@Composable
fun NewsListRoute(onItemClick: (Article) -> Unit) {
    NewsListScreen(onItemClick)
}

