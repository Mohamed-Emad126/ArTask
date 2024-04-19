package com.memad.artask.data.remote.models

data class ArticlesResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)