package com.donguyen.photoalbum.util.rx

import io.reactivex.ObservableTransformer

/**
 * Created by DoNguyen on 22/8/19.
 */
abstract class Transformer<T> : ObservableTransformer<T, T>