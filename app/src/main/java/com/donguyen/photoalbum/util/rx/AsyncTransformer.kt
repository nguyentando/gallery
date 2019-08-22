package com.donguyen.photoalbum.util.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by DoNguyen on 9/3/19.
 */
class AsyncTransformer<T> : Transformer<T>() {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}