package com.example.data.remote.datasource

import com.example.data.remote.api.NewsAPI
import com.example.core.models.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class NewsRemoteDataSource @Inject constructor(private val api: NewsAPI) {

    suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Response<NewsResponse> {
        return api.getBreakingNews(countryCode, pageNumber)
    }


    suspend fun searchForNews(
        searchQuery: String,
        pageNumber: Int
    ): Response<NewsResponse> {
        return api.searchForNews(searchQuery, pageNumber)
    }
}