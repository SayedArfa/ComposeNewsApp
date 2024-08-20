package com.example.newslist.ui

import com.example.core.Result
import com.example.core.models.Article

data class ArticleUiState(val article: Article, val isFavorite: Boolean)
data class NewsUiState(
    val articles: List<ArticleUiState> = emptyList(),
    val isLoading: Boolean = true,
    val error: Result.Error? = null
)