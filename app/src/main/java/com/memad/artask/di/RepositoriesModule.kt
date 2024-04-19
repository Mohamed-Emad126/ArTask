package com.memad.artask.di

import com.memad.artask.data.local.daos.ArticlesDao
import com.memad.artask.data.local.daos.FavoritesDao
import com.memad.artask.data.local.daos.HistorySearchDao
import com.memad.artask.data.remote.NewsApi
import com.memad.artask.di.annotations.FavRepo
import com.memad.artask.di.annotations.MainRepo
import com.memad.artask.repositories.BaseFavoriteRepo
import com.memad.artask.repositories.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {


    @MainRepo
    @Singleton
    @Provides
    fun provideMainRepository(
        articlesDao: ArticlesDao,
        newsApi: NewsApi,
        historySearchDao: HistorySearchDao,
        favoritesDao: FavoritesDao
    ): MainRepository {
        return MainRepository(
            newsApi,
            articlesDao,
            favoritesDao,
            historySearchDao
        )
    }


    @FavRepo
    @Singleton
    @Provides
    fun provideFavoritesRepository(
        articlesDao: ArticlesDao,
        newsApi: NewsApi,
        historySearchDao: HistorySearchDao,
        favoritesDao: FavoritesDao
    ): BaseFavoriteRepo {
        return MainRepository(
            newsApi,
            articlesDao,
            favoritesDao,
            historySearchDao
        )
    }
}