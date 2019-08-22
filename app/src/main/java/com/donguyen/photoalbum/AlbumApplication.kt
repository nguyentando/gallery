package com.donguyen.photoalbum

import android.app.Application
import com.donguyen.photoalbum.di.component.BaseComponent
import com.donguyen.photoalbum.di.component.DaggerBaseComponent
import com.donguyen.photoalbum.di.module.AlbumModule
import com.donguyen.photoalbum.di.module.AppModule
import com.donguyen.photoalbum.di.module.DataModule
import com.donguyen.photoalbum.di.module.NetworkModule

/**
 * Created by DoNguyen on 22/8/19.
 */
class AlbumApplication : Application() {

    private lateinit var baseComponent: BaseComponent

    override fun onCreate() {
        super.onCreate()
        initDependencies()
    }

    private fun initDependencies() {
        baseComponent = DaggerBaseComponent.builder()
            .appModule(AppModule(applicationContext))
            .dataModule(DataModule())
            .networkModule(NetworkModule())
            .build()
    }

    fun getBaseComponent() = baseComponent

    fun createAlbumComponent() = baseComponent.plus(AlbumModule())
}