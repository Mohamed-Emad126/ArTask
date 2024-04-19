package com.memad.artask.ui.screens._list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memad.artask.data.local.entities.ArticleEntity
import com.memad.artask.data.local.entities.FavoriteArticleEntity
import com.memad.artask.data.local.entities.SearchHistoryEntity
import com.memad.artask.di.annotations.MainRepo
import com.memad.artask.repositories.MainRepository
import com.memad.artask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    @MainRepo
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _searchHistory = MutableStateFlow(listOf<SearchHistoryEntity>())
    val searchHistory = _searchHistory.asStateFlow()

    private val _articlesList = MutableStateFlow(listOf<ArticleEntity>())
    val articlesList = _articlesList.asStateFlow()

    private val _favoriteArticles = MutableStateFlow(listOf<FavoriteArticleEntity>())
    val favoriteArticles = _favoriteArticles.asStateFlow()

    private var currPage = 1
    private val _endReached = MutableStateFlow(false)
    val endReached = _endReached.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    init {
        getSearchHistory()
        getFavoriteArticles()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToggleSearch(value: Boolean) {
        _isSearching.value = value
    }


    fun onSearchPerformed(text: String) {
        Log.d("NewsSearchBar", "--> Search performed: $text ${searchText.value}")
        if (text.isBlank()) return
        viewModelScope.launch {
            mainRepository.insertSearchHistory(text)
        }
        currPage = 1
        getNextPage(text)
    }

    fun getNextPage(text: String = searchText.value) {
        _isLoading.value = true
        if (currPage == 1){
            _articlesList.value = emptyList()
        }
        _endReached.value = _articlesList.value.size > 100 // 100 is the max number of articles to be fetched from the API using the free plan
        if (endReached.value) return
        viewModelScope.launch {
            mainRepository.getArticles(text, currPage)
                .distinctUntilChanged { old, new ->
                    old.data == new.data
                }.collectLatest {
                    Log.d("ArticlesViewModel", "--> Data for page $currPage: ${it.data}")
                    getList(it)
                }
        }
    }

    private fun getList(it: Resource<List<ArticleEntity>>) {
        when (it) {
            is Resource.Success -> {
                currPage++
                _isLoading.value = false
                Log.d("ArticlesViewModel", "--> ${it.data} -----")
                _articlesList.value += it.data!!
                endReached
            }

            is Resource.Error -> {
                _isLoading.value = false
                _error.value = it.error?.message ?: "An unexpected error occurred"
            }

            is Resource.Loading -> {
                _isLoading.value = true
            }
        }
    }

    fun onClearHistory() {
        viewModelScope.launch {
            mainRepository.clearSearchHistory()
        }
    }

    fun onClearOneHistoryItem(id: Int) {
        viewModelScope.launch {
            mainRepository.clearOneHistoryItem(id)
        }
    }

    fun favoriteArticle(article: ArticleEntity) {
        viewModelScope.launch {
            mainRepository.favoriteAnArticle(article.toFavoriteArticle())
        }
    }

    fun unFavoriteArticle(article: ArticleEntity) {
        viewModelScope.launch {
            mainRepository.unFavoriteAnArticle(articleId = article.id!!)
        }
    }

    private fun getFavoriteArticles() {
        viewModelScope.launch {
            mainRepository.getFavoritesArticles().collectLatest {
                _favoriteArticles.value = it
            }
        }
    }

    private fun getSearchHistory() {
        viewModelScope.launch {
            mainRepository.getSearchHistory().collectLatest {
                _searchHistory.value = it
            }
        }
    }

}