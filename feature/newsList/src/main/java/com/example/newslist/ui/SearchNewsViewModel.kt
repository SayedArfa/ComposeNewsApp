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
                                articles = it.data.articles
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
            emit(newsRepository.searchNews(text, searchNewsPage))
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
                            mutableListOf<Article>().apply {
                                addAll(_newsListFlow.value.articles + it.data.articles)
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


    /*private fun handleSearchNewsResponse(response: Result<NewsResponse>) {
        if (response is Result.Success) {
            response.data.let { resultResponse ->
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
    }*/
}












