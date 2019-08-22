package com.donguyen.photoalbum.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.donguyen.photoalbum.base.BaseViewModel
import com.donguyen.photoalbum.model.Photo
import com.donguyen.photoalbum.usecase.Failure
import com.donguyen.photoalbum.usecase.GetPhotosUseCase
import com.donguyen.photoalbum.usecase.Success
import com.donguyen.photoalbum.util.extension.exhaustive
import com.donguyen.photoalbum.view.AlbumViewState

/**
 * Created by DoNguyen on 22/8/19.
 */
class AlbumViewModel(private val getPhotosUseCase: GetPhotosUseCase) : BaseViewModel() {

    private val mViewState: MutableLiveData<AlbumViewState> = MutableLiveData()
    val viewState: LiveData<AlbumViewState> = mViewState

    init {
        mViewState.value = AlbumViewState()
    }

    fun loadPhotos(refresh: Boolean = false) {
        mViewState.value = viewState.value?.copy(loading = true)
        getPhotosUseCase.execute(GetPhotosUseCase.Input(refresh))
            .subscribe {
                when (it) {
                    is Success -> handleGetPhotosSuccess(it.data)
                    is Failure -> handleFailure(it.error)
                }.exhaustive
            }
            .autoClear()
    }

    private fun handleGetPhotosSuccess(photoList: PagedList<Photo>) {
        mViewState.value = viewState.value?.copy(
            loading = false,
            photoList = photoList,
            error = ""
        )
    }

    private fun handleFailure(error: String) {
        mViewState.value = viewState.value?.copy(
            loading = false,
            error = error
        )
    }
}