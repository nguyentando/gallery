package com.donguyen.photoalbum.model.api

import com.google.gson.annotations.SerializedName

/**
 * Created by DoNguyen on 9/3/19.
 */
data class Urls(

    @SerializedName("regular")
    var fullUrl: String,

    @SerializedName("thumb")
    var thumbUrl: String
)