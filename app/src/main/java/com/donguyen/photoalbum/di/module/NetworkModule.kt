package com.donguyen.photoalbum.di.module

import com.donguyen.photoalbum.model.api.HeaderInterceptor
import com.donguyen.photoalbum.model.api.PhotoApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by DoNguyen on 22/8/19.
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providePhotoApi(retrofit: Retrofit): PhotoApi {
        return retrofit.create(PhotoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor(CLIENT_ID))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    companion object {
        // TODO hide the client id
        private const val CLIENT_ID = "c7db782e6f37c021f0e9008ffd343da4e5bbf0b15e381838b2980f273ea080b5"
        private const val BASE_URL = "https://api.unsplash.com/"
    }
}