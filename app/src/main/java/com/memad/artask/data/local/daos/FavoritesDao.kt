package com.memad.artask.data.local.daos
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.memad.artask.data.local.entities.FavoriteArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_table")
    fun getFavorites(): Flow<List<FavoriteArticleEntity>>

    // favorite an article by it's id

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favoriteAnArticle(favoriteArticleEntity: FavoriteArticleEntity)

    // un favorite an article by it's id
    @Query("DELETE FROM favorite_table WHERE id = :articleId")
    suspend fun unFavoriteAnArticle(articleId: Int): Int

    @Query("SELECT Count(*) FROM favorite_table WHERE id = :articleId")
    suspend fun checkIsFavorite(articleId: Int): Int

}