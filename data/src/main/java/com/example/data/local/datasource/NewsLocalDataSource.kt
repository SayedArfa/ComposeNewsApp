package com.example.data.local.datasource

import com.example.data.local.db.ArticleDao
import com.example.data.models.ArticleEntity
import javax.inject.Inject


class NewsLocalDataSource @Inject constructor(private val articleDao: ArticleDao) {
    suspend fun upsert(article: ArticleEntity) =
        articleDao.upsert(article)

    suspend fun getSavedNews(): List<ArticleEntity> =
        articleDao.getAllArticles()

    suspend fun deleteArticle(article: ArticleEntity) =
        articleDao.deleteArticle(article)

}