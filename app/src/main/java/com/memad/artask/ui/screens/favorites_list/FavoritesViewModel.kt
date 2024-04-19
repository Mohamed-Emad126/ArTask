package com.memad.artask.ui.screens.favorites_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memad.artask.data.local.entities.FavoriteArticleEntity
import com.memad.artask.di.annotations.FavRepo
import com.memad.artask.repositories.BaseFavoriteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    @FavRepo
    private val favoritesRepository: BaseFavoriteRepo
) : ViewModel() {
    private val _favoriteArticles = MutableStateFlow(listOf<FavoriteArticleEntity>())
    val favoriteArticles = _favoriteArticles.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        getFavoriteArticles()
    }

    private fun getFavoriteArticles() {
        _isLoading.value = true
        viewModelScope.launch {
            favoritesRepository.getFavoritesArticles().collectLatest { result ->
                _favoriteArticles.value = result
                _isLoading.value = false
            }
        }
    }

    fun unFavoriteArticle(article: FavoriteArticleEntity) {
        viewModelScope.launch {
            favoritesRepository.unFavoriteAnArticle(article.id)
        }
    }

}