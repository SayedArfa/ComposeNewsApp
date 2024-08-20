package com.example.newslist.ui


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.Result
import com.example.core.models.Article
import com.example.core.models.NewsResponse
import com.example.core.repository.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepo
) : ViewModel() {
    private val _newsListFlow: MutableStateFlow<NewsUiState> = MutableStateFlow(NewsUiState())
    val newsListFlow: StateFlow<NewsUiState> = _newsListFlow
    private var breakingNewsPage = 1

    init {
        viewModelScope.launch {
            getSavedNews()
            getBreakingNews()
        }
    }

    private suspend fun getBreakingNews() {
        _newsListFlow.value = _newsListFlow.value.copy(isLoading = true)
        val response = newsRepository.getBreakingNews("us", breakingNewsPage)
        Log.d("loadMore", "getBreakingNews: $breakingNewsPage")
        handleBreakingNewsResponse(response)
    }

    fun loadMore() {
        viewModelScope.launch {
            getBreakingNews()
        }
    }

    fun retry() {
        viewModelScope.launch {
            getBreakingNews()
        }
    }


    private fun handleBreakingNewsResponse(response: Result<NewsResponse>) {
        when (response) {
            is Result.Success -> {
                response.data.let { resultResponse ->
                    breakingNewsPage++
                    _newsListFlow.value = _newsListFlow.value.copy(isLoading = false,
                        error = null,
                        articles = mutableListOf<ArticleUiState>().apply {
                            addAll(_newsListFlow.value.articles + resultResponse.articles.map {
                                ArticleUiState(it, savedNews.map { it.url }.contains(it.url))
                            })
                        })
                }
            }

            is Result.Error -> _newsListFlow.value =
                _newsListFlow.value.copy(error = response, isLoading = false)

            Result.Loading -> {}
        }

    }

    private fun refreshNews() {
        val refreshedState = _newsListFlow.value.copy(articles = _newsListFlow.value.articles.map {
            ArticleUiState(it.article, savedNews.map { it.url }.contains(it.article.url))
        })
        _newsListFlow.value = refreshedState
    }

    private var savedNews: Set<Article> = setOf()

    private suspend fun getSavedNews() {
        savedNews = newsRepository.getSavedNews().toSet()
    }

    fun addRemoveArticle(article: Article) = viewModelScope.launch {
        if (savedNews.map { it.url }.contains(article.url)) newsRepository.deleteArticle(article)
        else newsRepository.upsert(article)
        getSavedNews()
        refreshNews()
    }
}












