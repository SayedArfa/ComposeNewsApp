package com.example.newslist.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.Result
import com.example.core.models.Article
import com.example.core.models.NewsResponse
import com.example.core.repository.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepo
) : ViewModel() {
    val searchQuery = MutableStateFlow("")
    private var searchNewsPage = 1


    private val _newsListFlow: MutableStateFlow<NewsUiState> =
        MutableStateFlow(NewsUiState())
    val newsListFlow: Flow<NewsUiState> = _newsListFlow

    init {
        viewModelScope.launch {
            getSavedNews()
            searchQuery.debounce(500L)
                .flatMapConcat {
                    searchNews(it)
                }.collectLatest {
                    searchNewsPage = 1
                    when (it) {
                        is Result.Success -> {
                            _newsListFlow.value = _newsListFlow.value.copy(
                                isLoading = false,
                                error = null,
                                articles = it.data.articles.map {
                                    ArticleUiState(it, savedNews.map { it.url }.contains(it.url))
                                }
                            )
                        }

                        is Result.Error -> _newsListFlow.value =
                            _newsListFlow.value.copy(error = it, isLoading = false)

                        Result.Loading -> {}
                    }
                }
        }
    }

    private suspend fun searchNews(text: String): Flow<Result<NewsResponse>> =
        flow {
            emit(Result.Loading)
            emit(
                if (text.isBlank()) Result.Success(
                    data = NewsResponse(
                        mutableListOf(),
                        "",
                        0
                    )
                ) else newsRepository.searchNews(text, searchNewsPage)
            )
        }

    fun loadMore() {
        viewModelScope.launch {
            searchNews(searchQuery.value).collectLatest {

                when (it) {
                    is Result.Success -> {
                        searchNewsPage++
                        _newsListFlow.value = _newsListFlow.value.copy(
                            isLoading = false,
                            error = null,
                            articles =
                            _newsListFlow.value.articles + it.data.articles.map {
                                ArticleUiState(it, savedNews.map { it.url }.contains(it.url))
                            }
                        )
                    }

                    is Result.Error -> _newsListFlow.value =
                        _newsListFlow.value.copy(error = it, isLoading = false)

                    Result.Loading -> {}
                }
            }
        }

    }

    fun retry() {
        val retrySearchQuery = searchQuery.value
        searchQuery.value = "xxxxx" // change the stateFlow value
        searchQuery.value = retrySearchQuery
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












