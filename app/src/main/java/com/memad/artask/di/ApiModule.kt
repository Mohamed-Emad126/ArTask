package com.memad.artask.di

import com.google.gson.Gson
import com.memad.artask.data.remote.NewsApi
import com.memad.artask.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(client.build())
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(cacheInterceptor: Interceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            this.addInterceptor(logging)

        }.addInterceptor(cacheInterceptor)
    }


    // Cache interceptor to cache the response for 1 day (86400 seconds)
    @Singleton
    @Provides
    fun provideCacheInterceptor() =
        Interceptor { chain ->
            val originalResponse: Response = chain.proceed(chain.request())
            originalResponse.newBuilder()
                .header("Cache-Control", "max-age=86400")
                .build()
        }


    @Singleton
    @Provides
    fun provideNewsApi(retrofit: Retrofit): NewsApi {
        return retrofit.create(NewsApi::class.java)
    }


    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }


}