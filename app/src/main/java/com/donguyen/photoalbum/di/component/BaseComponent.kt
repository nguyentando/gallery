package com.donguyen.photoalbum.di.component

import com.donguyen.photoalbum.di.module.AlbumModule
import com.donguyen.photoalbum.di.module.AppModule
import com.donguyen.photoalbum.di.module.DataModule
import com.donguyen.photoalbum.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by DoNguyen on 9/3/19.
 */
@Singleton
@Component(
    modules = [
        AppModule::class,
        DataModule::class,
        NetworkModule::class
    ]
)
interface BaseComponent {

    fun plus(albumModule: AlbumModule): AlbumSubComponent
}