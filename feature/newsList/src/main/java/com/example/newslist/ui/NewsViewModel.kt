package com.example.newslist.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.Result
import com.example.core.models.Article
import com.example.core.models.NewsResponse
import com.example.core.repository.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepo
) : ViewModel() {
    private val _breakingNews: MutableStateFlow<Result<NewsResponse>> =
        MutableStateFlow(Result.Loading)
    val breakingNews: Flow<Result<NewsResponse>> = _breakingNews
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Result<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null


    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        handleBreakingNewsResponse(response)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        newSearchQuery = searchQuery
        searchNews.postValue(Result.Loading)
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        handleSearchNewsResponse(response)
    }

    private fun handleBreakingNewsResponse(response: Result<NewsResponse>) {
        if (response is Result.Success) {
            response.data?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                _breakingNews.value = Result.Success(breakingNewsResponse ?: resultResponse)
            }
        } else
            _breakingNews.value = response
    }


    private fun handleSearchNewsResponse(response: Result<NewsResponse>) {
        if (response is Result.Success) {
            response.data?.let { resultResponse ->
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }

                searchNews.postValue(Result.Success(searchNewsResponse ?: resultResponse))
            }
        } else
            searchNews.postValue(response)
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
        getSavedNews()
    }

    private val _savedNews = MutableLiveData<List<Article>>()
    val savedNews: LiveData<List<Article>> = _savedNews
    fun getSavedNews() {
        viewModelScope.launch {
            _savedNews.postValue(newsRepository.getSavedNews())
        }
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
        getSavedNews()
    }
}












