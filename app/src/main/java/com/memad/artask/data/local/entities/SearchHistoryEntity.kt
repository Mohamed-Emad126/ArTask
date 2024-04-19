package com.memad.artask.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val searchQuery: String,
    val searchTime: Long
){
    fun timeAgo(): String {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - searchTime
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            else -> "$days days ago"
        }
    }
}