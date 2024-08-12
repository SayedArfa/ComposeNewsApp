package com.example.core.repository

import com.example.core.Result
import com.example.core.models.Article
import com.example.core.models.NewsResponse

interface NewsRepo {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Result<NewsResponse>

    suspend fun searchNews(searchQuery: String, pageNumber: Int): Result<NewsResponse>

    suspend fun upsert(article: Article): Long

    suspend fun getSavedNews(): List<Article>

    suspend fun deleteArticle(article: Article)
}