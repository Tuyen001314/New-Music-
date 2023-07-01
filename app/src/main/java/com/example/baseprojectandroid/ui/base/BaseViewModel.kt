package com.example.baseprojectandroid.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {
    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    fun CoroutineScope.launchSafe(
        coroutineContext: CoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val job = SupervisorJob() + coroutineContext
        launch(job + coroutineExceptionHandler, start, block)
    }
}

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
}

fun CoroutineScope.launchSafe(
    coroutineContext: CoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) {
    val job = SupervisorJob() + coroutineContext
    launch(job + coroutineExceptionHandler, start, block)
}