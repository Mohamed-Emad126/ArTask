package com.memad.artask.data.remote

import com.memad.artask.data.remote.models.ArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("everything")
    suspend fun getArticles(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sortBy") sortBy: String
    ): ArticlesResponse
}