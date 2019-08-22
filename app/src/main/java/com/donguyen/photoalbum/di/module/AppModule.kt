package com.donguyen.photoalbum.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by DoNguyen on 22/8/19.
 */
@Module
class AppModule(context: Context) {

    private val appContext = context.applicationContext

    @Provides
    @Singleton
    fun provideAppContext(): Context {
        return appContext
    }
}