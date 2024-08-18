package com.example.newslist.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.models.Article

@Composable
fun NewsListRoute(viewModel: NewsViewModel = hiltViewModel(), onItemClick: (Article) -> Unit) {
    NewsListScreen(onItemClick, viewModel.breakingNews)
}

