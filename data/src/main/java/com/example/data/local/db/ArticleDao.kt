package com.example.data.local.db

import androidx.room.*
import com.example.data.models.ArticleEntity

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(articleEntity: ArticleEntity): Long

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Delete
    suspend fun deleteArticle(articleEntity: ArticleEntity)
}