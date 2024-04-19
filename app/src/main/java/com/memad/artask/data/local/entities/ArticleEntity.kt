package com.memad.artask.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.memad.artask.data.remote.models.Article
import com.memad.artask.data.remote.models.Source
import com.memad.artask.utils.ARTICLES_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = ARTICLES_TABLE)
data class ArticleEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val author: String?,
    val content: String,
    val description: String,
    val publishedAt: String?,
    val source: Source? = Source("", ""),
    val title: String,
    val url: String?,
    val urlToImage: String?,
    val query: String,
    val page:Int
) : Parcelable {
    fun toArticle(): Article {
        return Article(
            author = author?:"",
            content = content,
            description = description,
            publishedAt = publishedAt?:"",
            source = source?: Source("", ""),
            title = title,
            url = url?:"",
            urlToImage = urlToImage?:""
        )
    }
    fun toFavoriteArticle(): FavoriteArticleEntity {
        return FavoriteArticleEntity(
            id = id!!,
            author = author?:"",
            content = content,
            description = description,
            publishedAt = publishedAt?:"",
            source = source?: Source("", ""),
            title = title,
            url = url?:"",
            urlToImage = urlToImage?:""
        )
    }
}