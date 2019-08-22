package com.donguyen.photoalbum.di.module

import com.donguyen.photoalbum.model.repo.PhotoRepository
import com.donguyen.photoalbum.usecase.GetPhotosUseCase
import com.donguyen.photoalbum.util.rx.AsyncTransformer
import com.donguyen.photoalbum.viewmodel.AlbumVMFactory
import dagger.Module
import dagger.Provides

/**
 * Created by DoNguyen on 9/3/19.
 */
@Module
class AlbumModule {

    @Provides
    fun provideAlbumVMFactory(getPhotosUseCase: GetPhotosUseCase) = AlbumVMFactory(getPhotosUseCase)

    @Provides
    fun provideGetPhotosUseCase(photoRepository: PhotoRepository) =
        GetPhotosUseCase(photoRepository, AsyncTransformer())
}