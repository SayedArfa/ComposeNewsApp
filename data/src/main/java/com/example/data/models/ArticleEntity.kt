package com.example.data.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.models.Source
import java.io.Serializable

@Entity(
    tableName = "articles"
)
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
//    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Serializable