package com.donguyen.photoalbum.util.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.donguyen.photoalbum.AlbumApplication
import com.donguyen.photoalbum.di.component.BaseComponent

/**
 * Created by DoNguyen on 9/3/19.
 */
val Context.albumApplication: AlbumApplication
    get() = applicationContext as AlbumApplication

val Context.baseComponent: BaseComponent
    get() = albumApplication.getBaseComponent()

fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    return Toast.makeText(this, text, duration).show()
}

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    return Toast.makeText(this, resId, duration).show()
}