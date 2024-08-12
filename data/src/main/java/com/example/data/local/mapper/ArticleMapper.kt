package com.example.data.local.mapper


import com.example.data.models.ArticleEntity
import com.example.core.models.Article

class ArticleMapper : Mapper<ArticleEntity, Article> {
    override fun mapFromEntity(type: ArticleEntity): Article {
        return Article(
            type.id,
            type.author,
            type.content,
            type.description,
            type.publishedAt,
//            type.source,
            type.title,
            type.url,
            type.urlToImage
        )
    }

    override fun mapToEntity(type: Article): ArticleEntity {
        return ArticleEntity(
            type.id,
            type.author,
            type.content,
            type.description,
            type.publishedAt,
//            type.source,
            type.title,
            type.url,
            type.urlToImage
        )
    }
}