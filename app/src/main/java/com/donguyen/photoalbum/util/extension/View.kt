package com.donguyen.photoalbum.util.extension

import android.view.View

/**
 * Created by DoNguyen on 22/8/19.
 */
fun View.isVisible() = this.visibility == View.VISIBLE

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.show(isShow: Boolean) {
    when (isShow) {
        true -> show()
        false -> gone()
    }.exhaustive
}