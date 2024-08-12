package com.example.data.di

import com.example.core.repository.NewsRepo
import com.example.core.util.BaseNetworkHelper
import com.example.core.util.NetworkHelper
import com.example.data.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Singleton
    @Binds
    abstract fun bindNewsRepo(repo: NewsRepository): NewsRepo

    /*@Singleton
    @Binds
    abstract fun bindNetworkHelper(networkHelper: NetworkHelper): BaseNetworkHelper*/
}