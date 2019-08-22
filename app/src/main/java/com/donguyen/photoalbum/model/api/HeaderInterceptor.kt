package com.donguyen.photoalbum.model.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by DoNguyen on 22/8/19.
 */
class HeaderInterceptor(private val clientId: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Client-ID $clientId")
            .build()
        return chain.proceed(request)
    }
}