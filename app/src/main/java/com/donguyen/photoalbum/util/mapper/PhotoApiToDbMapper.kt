package com.donguyen.photoalbum.util.mapper

import com.donguyen.photoalbum.model.api.PhotoApiData
import com.donguyen.photoalbum.model.db.PhotoData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by DoNguyen on 9/3/19.
 */
@Singleton
class PhotoApiToDbMapper @Inject constructor() : Mapper<PhotoApiData, PhotoData> {

    override fun mapFrom(from: PhotoApiData): PhotoData {
        return PhotoData(
            id = from.id,
            width = from.width,
            height = from.height,
            fullUrl = from.urls.fullUrl,
            thumbUrl = from.urls.thumbUrl
        )
    }
}