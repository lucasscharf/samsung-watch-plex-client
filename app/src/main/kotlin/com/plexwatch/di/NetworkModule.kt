package com.plexwatch.di

import com.plexwatch.data.api.PlexAuthApi
import com.plexwatch.data.api.PlexMediaApi
import com.plexwatch.data.api.PlexServerApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            )
            .build()
    }

    @Provides
    @Singleton
    @Named("plex_tv")
    fun providePlexTvRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PlexAuthApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun providePlexAuthApi(
        @Named("plex_tv") retrofit: Retrofit,
    ): PlexAuthApi = retrofit.create(PlexAuthApi::class.java)

    @Provides
    @Singleton
    fun providePlexServerApi(
        @Named("plex_tv") retrofit: Retrofit,
    ): PlexServerApi = retrofit.create(PlexServerApi::class.java)

    @Provides
    @Singleton
    fun providePlexMediaApi(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): PlexMediaApi {
        // Media API uses dynamic base URL based on selected server
        // For now, we'll create a basic instance; actual calls will use the server URL
        return Retrofit.Builder()
            .baseUrl("http://localhost:32400/") // Placeholder, actual URL comes from server
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PlexMediaApi::class.java)
    }
}
