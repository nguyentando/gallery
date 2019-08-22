package com.donguyen.photoalbum.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign

/**
 * Created by DoNguyen on 9/3/19.
 */
open class BaseViewModel : ViewModel() {

    private val subscriptions: CompositeDisposable = CompositeDisposable()

    protected fun Disposable.autoClear() {
        subscriptions += this
    }

    private fun clearDisposables() {
        subscriptions.clear()
    }

    override fun onCleared() {
        clearDisposables()
    }
}