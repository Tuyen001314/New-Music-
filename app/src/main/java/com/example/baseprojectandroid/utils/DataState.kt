package com.example.baseprojectandroid.utils

sealed class DataState<out T>(val message: String? = null, val data: T? = null) {
    object Loading: DataState<Nothing>()
    class Processing<T>(val process: Float): DataState<T>()
    class Failure(message: String? = null, val error: Throwable? = null): DataState<Nothing>(message)
    class Success<out T> (message: String? = null, data: T? = null): DataState<T>(message, data)

    fun whenLoading(callback: (DataState<T>) -> Unit) = apply {
        if (this is Loading) {
            callback(this)
        }
    }

    fun whenProcessing(callback: (DataState<T>) -> Unit) = apply {
        if (this is Processing) {
            callback(this)
        }
    }

    fun whenFailure(callback: (DataState<T>) -> Unit) = apply {
        if (this is Failure) {
            callback(this)
        }
    }

    fun whenSuccess(callback: (DataState<T>) -> Unit) = apply {
        if (this is Success) {
            callback(this)
        }
    }
}