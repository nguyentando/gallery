package com.donguyen.photoalbum.model.api

/**
 * Created by DoNguyen on 22/8/19.
 */
data class PhotoApiData(
    var id: String,
    var width: Int,
    var height: Int,
    var urls: Urls
)