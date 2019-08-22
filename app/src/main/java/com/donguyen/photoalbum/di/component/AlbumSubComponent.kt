package com.donguyen.photoalbum.di.component

import com.donguyen.photoalbum.di.module.AlbumModule
import com.donguyen.photoalbum.di.scope.PerActivity
import com.donguyen.photoalbum.view.AlbumActivity
import dagger.Subcomponent

/**
 * Created by DoNguyen on 22/8/19.
 */
@PerActivity
@Subcomponent(modules = [AlbumModule::class])
interface AlbumSubComponent {

    fun inject(albumActivity: AlbumActivity)
}