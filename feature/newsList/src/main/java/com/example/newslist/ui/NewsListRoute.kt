package com.example.newslist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.common.ui.TitleBar
import com.example.core.models.Article

@Composable
fun NewsListRoute(
    viewModel: NewsViewModel = hiltViewModel(),
    onItemClick: (Article) -> Unit,
    onShowSnackBar: suspend (String, String?, () -> Unit) -> Unit
) {
    Column {
        TitleBar(title = "Home") {

        }
        Spacer(modifier = Modifier.size(10.dp))
        NewsListScreen(
            viewModel.newsListFlow.collectAsState(initial = NewsUiState()).value,
            onItemClick,
            { viewModel.addRemoveArticle(it) },
            { viewModel.retry() },
            { viewModel.loadMore() },
            onShowSnackBar
        )
    }

}

