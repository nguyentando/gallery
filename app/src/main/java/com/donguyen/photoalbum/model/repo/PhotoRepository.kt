package com.donguyen.photoalbum.model.repo

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.donguyen.photoalbum.model.Photo
import com.donguyen.photoalbum.model.api.PhotoApi
import com.donguyen.photoalbum.model.api.PhotoApiData
import com.donguyen.photoalbum.model.db.AppDatabase
import com.donguyen.photoalbum.model.db.PhotoDao
import com.donguyen.photoalbum.model.db.PhotoData
import com.donguyen.photoalbum.usecase.Result
import com.donguyen.photoalbum.util.mapper.Mapper
import io.reactivex.Observable

/**
 * Created by DoNguyen on 9/3/19.
 */
class PhotoRepository(
    private val photoDao: PhotoDao,
    private val photoApi: PhotoApi,
    private val photoDataToPhotoMapper: Mapper<PhotoData, Photo>,
    private val photoApiToDbMapper: Mapper<PhotoApiData, PhotoData>
) {

    /**
     * Get all photos in the database (lazy loading)
     * @return a [Result] of all photos with lazy loading supported by wrapping in a [PagedList]
     */
    fun getPhotos(refresh: Boolean): Observable<Result<PagedList<Photo>>> {
        val dataSourceFactory = photoDao.getPhotos()
            .map(photoDataToPhotoMapper::mapFrom)

        val pagingConfig = PagedList.Config.Builder()
            .setPageSize(AppDatabase.PAGE_SIZE)
            .setPrefetchDistance(AppDatabase.PREFETCH_DISTANCE)
            .setInitialLoadSizeHint(AppDatabase.INITIAL_LOAD_SIZE)
            .setEnablePlaceholders(false)
            .build()

        val boundaryCallback = PhotoBoundaryCallback(refresh, photoDao, photoApi, photoApiToDbMapper)

        val data = RxPagedListBuilder(dataSourceFactory, pagingConfig)
            .setBoundaryCallback(boundaryCallback)
            .buildObservable()
            .map {
                Result.success(it)
            }
            .doOnSubscribe {
                if (refresh) boundaryCallback.requestAndSaveData()
            }

        return Observable.merge(data, boundaryCallback.errors)
    }
}