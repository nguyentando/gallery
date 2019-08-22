package com.donguyen.photoalbum.usecase

import androidx.paging.PagedList
import com.donguyen.photoalbum.model.Photo
import com.donguyen.photoalbum.model.repo.PhotoRepository
import com.donguyen.photoalbum.util.rx.Transformer
import io.reactivex.Observable

/**
 * Created by DoNguyen on 9/3/19.
 */
class GetPhotosUseCase(
    private val photoRepository: PhotoRepository,
    transformer: Transformer<Result<PagedList<Photo>>>? = null
) : UseCase<GetPhotosUseCase.Input, PagedList<Photo>>(transformer) {

    override fun buildObservable(input: Input): Observable<Result<PagedList<Photo>>> {
        return photoRepository.getPhotos(input.refresh)
    }

    data class Input(val refresh: Boolean = false)
}