package com.memad.artask.data.local.entities


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.memad.artask.data.remote.models.Source
import com.memad.artask.utils.FAVORITE_ARTICLES_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = FAVORITE_ARTICLES_TABLE)
data class FavoriteArticleEntity (
    @PrimaryKey
    val id: Int,
    val author: String?,
    val content: String,
    val description: String,
    val publishedAt: String?,
    val source: Source? = Source("", ""),
    val title: String,
    val url: String?,
    val urlToImage: String?
) : Parcelable