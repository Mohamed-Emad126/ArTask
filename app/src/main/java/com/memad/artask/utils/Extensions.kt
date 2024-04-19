package com.memad.artask.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import com.memad.artask.data.local.entities.ArticleEntity
import com.memad.artask.data.remote.models.ArticlesResponse
import com.memad.artask.data.remote.models.Source
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun ArticlesResponse.toArticleEntity(query: String, page:Int): List<ArticleEntity> {
    return this.articles.map {
        ArticleEntity(
            title = it.title?:"",
            author = it.author,
            url = it.url,
            urlToImage = it.urlToImage,
            publishedAt = it.publishedAt,
            content = it.content?:"",
            description = it.description?:"",
            query = query,
            source = it.source ?: Source("", ""),
            page = page
        )
    }
}

fun Modifier.bounceClickable(
    minScale: Float = 0.5f,
    onAnimationFinished: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) minScale else 1f,
        label = ""
    ) {
        if (isPressed) {
            isPressed = false
            onAnimationFinished?.invoke()
        }
    }

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable {
            isPressed = true
            onClick?.invoke()
        }
}

fun convertIsoDateToReadableFormat(isoDate: String): String {
    val instant = Instant.parse(isoDate)
    val zonedDateTime = instant.atZone(ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    return zonedDateTime.format(formatter)
}