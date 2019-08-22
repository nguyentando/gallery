package com.donguyen.photoalbum.util.mapper

import com.donguyen.photoalbum.model.Photo
import com.donguyen.photoalbum.model.db.PhotoData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by DoNguyen on 22/8/19.
 */
@Singleton
class PhotoDataToPhotoMapper @Inject constructor() : Mapper<PhotoData, Photo> {

    override fun mapFrom(from: PhotoData): Photo {
        return Photo(
            id = from.id,
            width = from.width,
            height = from.height,
            fullUrl = from.fullUrl,
            thumbUrl = from.thumbUrl
        )
    }
}