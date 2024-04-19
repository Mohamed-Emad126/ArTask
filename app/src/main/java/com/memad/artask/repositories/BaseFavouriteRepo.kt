package com.memad.artask.repositories

import com.memad.artask.data.local.entities.FavoriteArticleEntity
import kotlinx.coroutines.flow.Flow

interface BaseFavoriteRepo {

    suspend fun favoriteAnArticle(favoriteArticleEntity: FavoriteArticleEntity)

    suspend fun unFavoriteAnArticle(articleId: Int): Int

    suspend fun checkIsFavorite(articleId: Int): Boolean

    suspend fun getFavoritesArticles(): Flow<List<FavoriteArticleEntity>>

}
