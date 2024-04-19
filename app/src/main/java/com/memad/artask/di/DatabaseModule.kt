package com.memad.artask.di

import android.content.Context
import androidx.room.Room
import com.memad.artask.data.local.NewsDatabase
import com.memad.artask.data.local.daos.ArticlesDao
import com.memad.artask.data.local.daos.FavoritesDao
import com.memad.artask.data.local.daos.HistorySearchDao
import com.memad.artask.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Synchronized // to make sure that the database is created only once
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        NewsDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideNewsDao(newsDB: NewsDatabase): ArticlesDao {
        return newsDB.getNewsDao()
    }

    @Singleton
    @Provides
    fun provideSearchHistoryDao(newsDB: NewsDatabase): HistorySearchDao {
        return newsDB.getHistoryDao()
    }

    @Singleton
    @Provides
    fun provideFavoritesDao(newsDB: NewsDatabase): FavoritesDao {
        return newsDB.getFavoritesDao()
    }
}