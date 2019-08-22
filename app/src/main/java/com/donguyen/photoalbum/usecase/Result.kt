package com.donguyen.photoalbum.usecase

/**
 * Created by DoNguyen on 22/8/19.
 */
sealed class Result<T> {
    companion object {
        fun <T> success(data: T): Success<T> {
            return Success(data = data)
        }

        fun <T> failure(error: String): Failure<T> {
            return Failure(error = error)
        }
    }
}

data class Success<T>(val data: T) : Result<T>()

data class Failure<T>(val error: String) : Result<T>()