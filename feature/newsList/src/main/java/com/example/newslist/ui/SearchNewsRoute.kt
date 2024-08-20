package com.example.newslist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.models.Article

@Composable
fun SearchNewsRoute(
    onItemClick: (Article) -> Unit,
    onShowSnackBar: suspend (String, String?, () -> Unit) -> Unit
) {
    NewsSearchScreen(onItemClick, onShowSnackBar)
}

@Composable
fun NewsSearchScreen(
    onItemClick: (Article) -> Unit,
    onShowSnackBar: suspend (String, String?, () -> Unit) -> Unit
) {
    val viewModel: SearchNewsViewModel = hiltViewModel()
    Column {
        TextField(
            value = viewModel.searchQuery.collectAsState().value,

            onValueChange = {
                viewModel.searchQuery.value = it
            },
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            trailingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

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

