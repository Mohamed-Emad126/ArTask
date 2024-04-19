package com.memad.artask.repositories

import com.memad.artask.data.local.daos.FavoritesDao
import com.memad.artask.data.local.entities.ArticleEntity
import com.memad.artask.data.local.entities.FavoriteArticleEntity
import com.memad.artask.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class FavoritesRepository(
    private val favoritesDao: FavoritesDao
) : BaseFavoriteRepo{
    override suspend fun favoriteAnArticle(favoriteArticleEntity: FavoriteArticleEntity) {
        Mutex().withLock { // Lock the coroutine until the operation is done
            return favoritesDao.favoriteAnArticle(favoriteArticleEntity)
        }
    }

    override suspend fun unFavoriteAnArticle(articleId: Int): Int {
        Mutex().withLock { // Lock the coroutine until the operation is done
            return favoritesDao.unFavoriteAnArticle(articleId)
        }
    }

    override suspend fun checkIsFavorite(articleId: Int): Boolean {
        return favoritesDao.checkIsFavorite(articleId) > 0
    }

    override suspend fun getFavoritesArticles(): Flow<List<FavoriteArticleEntity>> {
        return favoritesDao.getFavorites()
    }

    abstract suspend fun getArticles(query: String,page: Int): Flow<Resource<List<ArticleEntity>>>


}