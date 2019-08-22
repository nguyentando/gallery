package com.donguyen.photoalbum.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by DoNguyen on 22/8/19.
 */
@Entity(tableName = "photos")
data class PhotoData(

    @PrimaryKey
    val id: String,

    val width: Int,

    val height: Int,

    @ColumnInfo(name = "full_url")
    val fullUrl: String,

    @ColumnInfo(name = "thumb_url")
    val thumbUrl: String
)