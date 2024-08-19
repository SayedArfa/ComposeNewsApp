package com.example.newslist.ui

import com.example.core.Result
import com.example.core.models.Article

data class NewsUiState(val articles: List<Article> = emptyList(), val isLoading: Boolean = true, val error: Result.Error? = null)