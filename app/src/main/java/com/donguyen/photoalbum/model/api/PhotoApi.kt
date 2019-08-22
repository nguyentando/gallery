package com.donguyen.photoalbum.model.api

import com.donguyen.photoalbum.model.db.AppDatabase
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by DoNguyen on 22/8/19.
 */
interface PhotoApi {

    @GET("photos")
    fun getPhotos(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int? = AppDatabase.PAGE_SIZE
    ): Single<List<PhotoApiData>>
}