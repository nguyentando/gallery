package com.donguyen.photoalbum.model

/**
 * Created by DoNguyen on 22/8/19.
 */
data class Photo(
    var id: String,
    var width: Int,
    var height: Int,
    var fullUrl: String,
    var thumbUrl: String
)