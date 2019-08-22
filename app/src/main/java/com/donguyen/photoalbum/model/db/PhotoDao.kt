package com.donguyen.photoalbum.model.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single

/**
 * Created by DoNguyen on 9/3/19.
 */
@Dao
interface PhotoDao : BaseDao<PhotoData> {

    @Query("SELECT * FROM photos")
    fun getPhotos(): DataSource.Factory<Int, PhotoData>

    @Query("SELECT COUNT(*) FROM photos")
    fun getCount(): Single<Int>

    @Query("DELETE FROM photos")
    fun clearTable()
}