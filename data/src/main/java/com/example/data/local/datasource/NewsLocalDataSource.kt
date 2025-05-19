package com.example.data.local.datasource

import com.example.data.local.db.ArticleDao
import com.example.data.models.ArticleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class NewsLocalDataSource @Inject constructor(private val articleDao: ArticleDao) {
    suspend fun upsert(article: ArticleEntity) =
        articleDao.upsert(article)

    suspend fun getSavedNews(): List<ArticleEntity> =
        articleDao.getAllArticles()

    fun getFavoritesFlow(): Flow<List<ArticleEntity>> =
        articleDao.getArticlesFlow()

    suspend fun deleteArticle(articleUrl: String?) =
        articleDao.deleteArticle(articleUrl)

}