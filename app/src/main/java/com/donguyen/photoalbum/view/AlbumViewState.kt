package com.donguyen.photoalbum.view

import androidx.paging.PagedList
import com.donguyen.photoalbum.model.Photo

/**
 * Created by DoNguyen on 9/3/19.
 */
data class AlbumViewState(
    var loading: Boolean = false,
    var error: String = "",
    var photoList: PagedList<Photo>? = null
)