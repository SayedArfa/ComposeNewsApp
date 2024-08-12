package com.example.data.repository

import com.example.core.Result
import com.example.core.models.Article
import com.example.core.models.NewsResponse
import com.example.core.repository.NewsRepo
import com.example.core.util.BaseNetworkHelper
import com.example.data.local.datasource.NewsLocalDataSource
import com.example.data.local.mapper.ArticleMapper
import com.example.data.remote.SafeApiCall
import com.example.data.remote.datasource.NewsRemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val networkHelper: BaseNetworkHelper
) : NewsRepo {
    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Result<NewsResponse> =
        SafeApiCall(networkHelper).invoke {
            newsRemoteDataSource.getBreakingNews(
                countryCode,
                pageNumber
            )
        }


    override suspend fun searchNews(searchQuery: String, pageNumber: Int): Result<NewsResponse> =
        SafeApiCall(networkHelper).invoke {
            newsRemoteDataSource.searchForNews(searchQuery, pageNumber)

        }

    override suspend fun upsert(article: Article) =
        newsLocalDataSource.upsert(ArticleMapper().mapToEntity(article))

    override suspend fun getSavedNews(): List<Article> =
        newsLocalDataSource.getSavedNews().map {
            ArticleMapper().mapFromEntity(it)
        }

    override suspend fun deleteArticle(article: Article) =
        newsLocalDataSource.deleteArticle(ArticleMapper().mapToEntity(article))


}