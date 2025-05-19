package com.example.newslist.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.models.Article
import com.example.core.repository.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val newsRepository: NewsRepo
) : ViewModel() {

    private val _favListFlow:
            Flow<List<Article>> = newsRepository.getFavoritesFlow()
    val favListFlow: Flow<List<Article>> = _favListFlow

    fun removeArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}
