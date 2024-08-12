package com.example.data.di


import android.content.Context
import androidx.room.Room
import com.example.core.repository.NewsRepo
import com.example.core.util.BaseNetworkHelper
import com.example.core.util.NetworkHelper
import com.example.data.local.db.ArticleDao
import com.example.data.local.db.ArticleDatabase
import com.example.data.remote.api.NewsAPI
import com.example.data.remote.api.RetrofitInstance
import com.example.data.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideArticleDatabase(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ArticleDatabase::class.java,
            "article_db.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideArticleDao(db: ArticleDatabase): ArticleDao {
        return db.getArticleDao()
    }

    @Singleton
    @Provides
    fun provideNewsApi(): NewsAPI {
        return RetrofitInstance.api
    }

    @Singleton
    @Provides
    fun bindNetworkHelper(@ApplicationContext context: Context): BaseNetworkHelper =
        NetworkHelper(context)
}