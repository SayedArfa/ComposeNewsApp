package com.example.newslist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.models.Article

@Composable
fun NewsListRoute(
    viewModel: NewsViewModel = hiltViewModel(),
    onItemClick: (Article) -> Unit,
    onShowSnackBar: suspend (String, String?, () -> Unit) -> Unit
) {
    NewsListScreen(
        viewModel.newsListFlow.collectAsState(initial = NewsUiState()).value,
        onItemClick,
        { viewModel.retry() },
        { viewModel.loadMore() },
        onShowSnackBar
    )
}

