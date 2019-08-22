package com.donguyen.photoalbum.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.donguyen.photoalbum.createMockPagedList
import com.donguyen.photoalbum.model.Photo
import com.donguyen.photoalbum.usecase.GetPhotosUseCase
import com.donguyen.photoalbum.usecase.Result
import com.donguyen.photoalbum.view.AlbumViewState
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by DoNguyen on 22/8/19.
 */
@RunWith(MockitoJUnitRunner::class)
class AlbumViewModelTest {

    /**
     * To support running LiveData off-device
     * https://pbochenski.pl/blog/07-12-2017-testing_livedata.html
     */
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    // sut (system under test)
    private lateinit var albumViewModel: AlbumViewModel

    // dependencies
    @Mock
    private lateinit var getPhotosUseCase: GetPhotosUseCase

    // checkers
    private lateinit var viewObserver: Observer<AlbumViewState>

    @Before
    @Suppress("UNCHECKED_CAST")
    fun setUp() {
        viewObserver = mock(Observer::class.java) as Observer<AlbumViewState>
        albumViewModel = AlbumViewModel(getPhotosUseCase)
        albumViewModel.viewState.observeForever(viewObserver)
    }

    /* -----------------------------------------------------------------------------------------------------------------
    * Because AlbumViewModel responsibility is USING [getPhotosUseCase] to load photos, and update the [viewState] accordingly.
    * so for testing this class, we just need to verify if it updates the [viewState] properly under different
    * responses (success|failure) from [getPhotosUseCase].
    *
    * The real data loading logic will be tested at lower levels (in repository classes, api classes, dao classes)
    * --------------------------------------------------------------------------------------------------------------- */

    @Test
    fun init() {
        // WHEN albumViewModel is being initialized
        // THEN receive a AlbumViewState instance with default constructor params
        verify(viewObserver).onChanged(AlbumViewState())
        verifyZeroInteractions(viewObserver)
    }

    @Test
    fun loadPhotosSucceeded() {
        // GIVEN getPhotosUseCase return success
        val refresh = false
        val input = GetPhotosUseCase.Input(refresh)
        val result = Result.success(createMockPagedList<Photo>())
        `when`(getPhotosUseCase.execute(input))
            .thenReturn(Observable.just(result))

        // WHEN loadPhotos
        albumViewModel.loadPhotos(refresh)

        // THEN receive loading then receive success with correct data
        verify(getPhotosUseCase).execute(input)
        verify(viewObserver).onChanged(AlbumViewState(loading = true))
        verify(viewObserver).onChanged(
            AlbumViewState(
                loading = false,
                photoList = result.data,
                error = ""
            )
        )
    }

    @Test
    fun loadPhotosFailed() {
        // GIVEN getPhotosUseCase return failure
        val refresh = false
        val input = GetPhotosUseCase.Input(refresh)
        val error = "get photos failed"
        `when`(getPhotosUseCase.execute(input))
            .thenReturn(Observable.just(Result.failure(error)))

        // WHEN loadPhotos
        albumViewModel.loadPhotos(refresh)

        // THEN receive loading then receive failure with correct error message
        verify(getPhotosUseCase).execute(input)
        verify(viewObserver).onChanged(AlbumViewState(loading = true))
        verify(viewObserver).onChanged(
            albumViewModel.viewState.value?.copy(loading = false, error = error)
        )
    }
}