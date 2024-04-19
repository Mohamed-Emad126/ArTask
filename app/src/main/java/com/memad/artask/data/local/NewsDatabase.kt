package com.memad.artask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.memad.artask.data.local.converters.SourceConverter
import com.memad.artask.data.local.daos.ArticlesDao
import com.memad.artask.data.local.daos.FavoritesDao
import com.memad.artask.data.local.daos.HistorySearchDao
import com.memad.artask.data.local.entities.ArticleEntity
import com.memad.artask.data.local.entities.FavoriteArticleEntity
import com.memad.artask.data.local.entities.SearchHistoryEntity

@Database(
    entities = [ArticleEntity::class, SearchHistoryEntity::class, FavoriteArticleEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(SourceConverter::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): ArticlesDao
    abstract fun getHistoryDao(): HistorySearchDao
    abstract fun getFavoritesDao(): FavoritesDao
}
