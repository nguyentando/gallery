package com.donguyen.photoalbum.model.api

/**
 * Created by DoNguyen on 9/3/19.
 */
data class PhotoApiData(
    var id: String,
    var width: Int,
    var height: Int,
    var urls: Urls
)