package com.donguyen.photoalbum.di.module

import android.content.Context
import com.donguyen.photoalbum.model.api.PhotoApi
import com.donguyen.photoalbum.model.db.AppDatabase
import com.donguyen.photoalbum.model.db.PhotoDao
import com.donguyen.photoalbum.model.repo.PhotoRepository
import com.donguyen.photoalbum.util.mapper.PhotoApiToDbMapper
import com.donguyen.photoalbum.util.mapper.PhotoDataToPhotoMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by DoNguyen on 9/3/19.
 */
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun providePhotoDao(database: AppDatabase) = database.photoDao()

    @Provides
    @Singleton
    fun providePhotoRepository(
        photoDao: PhotoDao,
        photoApi: PhotoApi,
        photoDataToPhotoMapper: PhotoDataToPhotoMapper,
        photoApiToDbMapper: PhotoApiToDbMapper
    ): PhotoRepository {
        return PhotoRepository(photoDao, photoApi, photoDataToPhotoMapper, photoApiToDbMapper)
    }
}