package com.donguyen.photoalbum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.donguyen.photoalbum.usecase.GetPhotosUseCase

/**
 * Created by DoNguyen on 22/8/19.
 */
class AlbumVMFactory(private val getPhotosUseCase: GetPhotosUseCase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlbumViewModel(getPhotosUseCase) as T
    }
}