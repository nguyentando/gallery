package com.donguyen.photoalbum.model.repo

import android.annotation.SuppressLint
import androidx.paging.PagedList
import com.donguyen.photoalbum.model.Photo
import com.donguyen.photoalbum.model.api.PhotoApi
import com.donguyen.photoalbum.model.api.PhotoApiData
import com.donguyen.photoalbum.model.db.AppDatabase
import com.donguyen.photoalbum.model.db.PhotoDao
import com.donguyen.photoalbum.model.db.PhotoData
import com.donguyen.photoalbum.usecase.Failure
import com.donguyen.photoalbum.usecase.Result
import com.donguyen.photoalbum.util.mapper.Mapper
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * Created by DoNguyen on 9/3/19.
 */
class PhotoBoundaryCallback(
    private var isRefresh: Boolean = false,
    private val photoDao: PhotoDao,
    private val photoApi: PhotoApi,
    private val mapper: Mapper<PhotoApiData, PhotoData>
) : PagedList.BoundaryCallback<Photo>() {

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    val errors: PublishSubject<Failure<PagedList<Photo>>> = PublishSubject.create()

    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Photo) {
        requestAndSaveData()
    }

    @SuppressLint("CheckResult")
    fun requestAndSaveData() {
        if (isRequestInProgress) return
        isRequestInProgress = true

        if (isRefresh) {
            photoApi.getPhotos(1, AppDatabase.PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { mapper.mapFromList(it) }
                .subscribe(
                    {
                        photoDao.clearTable()
                        photoDao.insertItems(it)
                        isRequestInProgress = false
                        isRefresh = false
                    }, {
                        errors.onNext(Result.failure(it.message.orEmpty()))
                        isRequestInProgress = false
                        isRefresh = false
                    })
        } else {
            photoDao.getCount()
                .map { it / AppDatabase.PAGE_SIZE + 1 } // from count to page
                .flatMap { photoApi.getPhotos(it, AppDatabase.PAGE_SIZE) } // from page to photoList
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { mapper.mapFromList(it) }
                .subscribe(
                    {
                        photoDao.insertItems(it)
                        isRequestInProgress = false
                    }, {
                        errors.onNext(Result.failure(it.message.orEmpty()))
                        isRequestInProgress = false
                    })
        }
    }
}