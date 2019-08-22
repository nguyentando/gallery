package com.donguyen.photoalbum.model

/**
 * Created by DoNguyen on 9/3/19.
 */
data class Photo(
    var id: String,
    var width: Int,
    var height: Int,
    var fullUrl: String,
    var thumbUrl: String
)