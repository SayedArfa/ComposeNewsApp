package com.example.newslist.ui


import androidx.lifecycle.ViewModel
import com.example.core.Result
import com.example.core.models.NewsResponse
import com.example.core.repository.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepo
) : ViewModel() {
    val searchQuery = MutableStateFlow("")
    var searchNewsPage = 1

    val searchNews: Flow<Result<NewsResponse>>
        get() = searchQuery.debounce(500L).distinctUntilChanged().map {
            newsRepository.searchNews(it, searchNewsPage)
        }


    /* private fun handleSearchNewsResponse(response: Result<NewsResponse>) {
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












