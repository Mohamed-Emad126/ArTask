package com.memad.artask.repositories

import com.memad.artask.data.local.daos.ArticlesDao
import com.memad.artask.data.local.daos.FavoritesDao
import com.memad.artask.data.local.daos.HistorySearchDao
import com.memad.artask.data.local.entities.SearchHistoryEntity
import com.memad.artask.data.remote.NewsApi
import com.memad.artask.utils.API_KEY
import com.memad.artask.utils.ARTICLES_PAGE_SIZE
import com.memad.artask.utils.Resource
import com.memad.artask.utils.networkBoundResource
import com.memad.artask.utils.toArticleEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val articlesDao: ArticlesDao,
    favoritesDao: FavoritesDao,
    private val historySearchDao: HistorySearchDao
) : FavoritesRepository(favoritesDao) {
    override suspend fun getArticles(query: String, page: Int) = networkBoundResource(
        query = {
            val articlesFlow = articlesDao.getArticles(query, page = page)
            articlesFlow
        },
        fetch = {
            val response =
                newsApi.getArticles(query, API_KEY, page, ARTICLES_PAGE_SIZE, "publishedAt")
            response
        },
        saveFetchResult = { response ->
            coroutineScope {
                launch(Dispatchers.IO) {
                    articlesDao.deleteAll(query, page)
                    articlesDao.insertAll(response.toArticleEntity(query, page))
                }
            }
        },
        shouldFetch = { articles ->
            articles.isEmpty()
        }
    ).catch {
        emit(Resource.Error(it))
    }

    suspend fun insertSearchHistory(query: String) {
        val searchHistoryEntities = historySearchDao.getSearchHistory().first().filter {
            it.searchQuery == query
        }
        if (searchHistoryEntities.isEmpty()) {
            historySearchDao.insertSearchHistory(
                SearchHistoryEntity(
                    searchQuery = query,
                    searchTime = System.currentTimeMillis()
                )
            )
        } else {
            historySearchDao.updateSearchHistory(
                SearchHistoryEntity(
                    id = searchHistoryEntities.first().id,
                    searchQuery = query,
                    searchTime = System.currentTimeMillis()
                )
            )
        }

    }

    fun getSearchHistory() = historySearchDao.getSearchHistory()

    suspend fun clearSearchHistory() = historySearchDao.clearSearchHistory()
    suspend fun clearOneHistoryItem(id: Int) = historySearchDao.clearOneHistoryItem(id)
}

