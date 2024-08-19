package com.example.newslist.ui


import android.util.Log
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
    private val _newsListFlow: MutableStateFlow<NewsUiState> =
        MutableStateFlow(NewsUiState())
    val newsListFlow: Flow<NewsUiState> = _newsListFlow
    private var breakingNewsPage = 1

    init {
        getBreakingNews()
    }

    private fun getBreakingNews() = viewModelScope.launch {
        _newsListFlow.value = _newsListFlow.value.copy(isLoading = true)
        val response = newsRepository.getBreakingNews("us", breakingNewsPage)
        Log.d("loadMore", "getBreakingNews: $breakingNewsPage")
        handleBreakingNewsResponse(response)
    }

    fun loadMore() {
        getBreakingNews()
    }

    fun retry() {
        getBreakingNews()
    }


    private fun handleBreakingNewsResponse(response: Result<NewsResponse>) {
        when (response) {
            is Result.Success -> {
                response.data.let { resultResponse ->
                    breakingNewsPage++
                    _newsListFlow.value = _newsListFlow.value.copy(
                        isLoading = false,
                        error = null,
                        articles =
                        mutableListOf<Article>().apply {
                            addAll(_newsListFlow.value.articles + resultResponse.articles)
                        }
                    )
                }
            }

            is Result.Error -> _newsListFlow.value =
                _newsListFlow.value.copy(error = response, isLoading = false)

            Result.Loading -> {}
        }

    }

    /* fun saveArticle(article: Article) = viewModelScope.launch {
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
     }*/
}












