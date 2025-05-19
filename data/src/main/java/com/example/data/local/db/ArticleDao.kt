package com.example.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.models.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(articleEntity: ArticleEntity): Long

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Query("SELECT * FROM articles")
    fun getArticlesFlow(): Flow<List<ArticleEntity>>

    @Query("DELETE FROM articles where url = :articleUrl")
    suspend fun deleteArticle(articleUrl: String?)
}