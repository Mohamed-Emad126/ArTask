package com.memad.artask.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.memad.artask.data.local.entities.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorySearchDao {
    @Query("SELECT * FROM search_history ORDER BY searchTime DESC")
    fun getSearchHistory(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(searchHistoryEntity: SearchHistoryEntity)

    @Query("DELETE FROM search_history")
    suspend fun clearSearchHistory()

    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun clearOneHistoryItem(id: Int): Int

    @Update
    suspend fun updateSearchHistory(searchHistoryEntity: SearchHistoryEntity)
}