package com.plexwatch.data.api

import com.plexwatch.domain.repository.ServerRepository
import dagger.Lazy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DynamicBaseUrlInterceptor
    @Inject
    constructor(
        private val serverRepository: Lazy<ServerRepository>,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            // Only intercept requests to localhost (our placeholder)
            if (originalUrl.host != "localhost") {
                return chain.proceed(originalRequest)
            }

            val server =
                runBlocking {
                    serverRepository.get().getSelectedServer().first()
                }

            if (server == null) {
                return chain.proceed(originalRequest)
            }

            val newBaseUrl = server.baseUrl.toHttpUrlOrNull() ?: return chain.proceed(originalRequest)

            val newUrl =
                originalUrl.newBuilder()
                    .scheme(newBaseUrl.scheme)
                    .host(newBaseUrl.host)
                    .port(newBaseUrl.port)
                    .build()

            val newRequest =
                originalRequest.newBuilder()
                    .url(newUrl)
                    .build()

            return chain.proceed(newRequest)
        }
    }
