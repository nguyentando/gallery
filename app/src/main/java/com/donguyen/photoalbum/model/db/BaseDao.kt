package com.donguyen.photoalbum.model.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

/**
 * Created by DoNguyen on 9/3/19.
 */
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(items: List<T>)

    @Delete
    fun deleteItem(item: T): Int

    @Delete
    fun deleteItems(items: List<T>): Int
}