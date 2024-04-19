package com.memad.artask.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.memad.artask.data.local.entities.ArticleEntity
import com.memad.artask.utils.ARTICLES_PAGE_SIZE
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao {
    @Query("SELECT * FROM articles_table WHERE `query` LIKE :q AND page = :page LIMIT :pageSize ")
    fun getArticles(q: String, pageSize: Int = ARTICLES_PAGE_SIZE, page: Int): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(articles: List<ArticleEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity): Long

    @Query("DELETE FROM articles_table WHERE `query` = :q AND page = :page")
    suspend fun deleteAll(q: String, page: Int): Int

    @Query("SELECT * FROM articles_table WHERE title LIKE :query ")
    fun searchArticles(query: String): Flow<List<ArticleEntity>>

}