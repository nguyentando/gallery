package com.donguyen.photoalbum.util.rx

import io.reactivex.ObservableTransformer

/**
 * Created by DoNguyen on 9/3/19.
 */
abstract class Transformer<T> : ObservableTransformer<T, T>