package com.multazamgsd.glimovie.di

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.multazamgsd.glimovie.App
import com.multazamgsd.glimovie.BuildConfig
import com.multazamgsd.glimovie.data.remote.network.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModules {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val AUTH_HEADER = "Authorization"
    }

    @Provides
    @Singleton
    fun providesHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(FlipperOkhttpInterceptor(App.networkFlipperPlugin))
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader(AUTH_HEADER, "Bearer ${BuildConfig.API_KEY}")
                        .build()
                )
            }
            .build()

    @Provides
    @Singleton
    fun providesMovieService(okHttpClient: OkHttpClient): MovieService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(MovieService::class.java)
}