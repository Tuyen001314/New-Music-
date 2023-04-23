package com.example.baseprojectandroid.data

data class ErrorResponse(
    val code: Int,
    override val cause: Throwable?,
    override val message: String?, val isOnline: Boolean = true, val url: String? = null
) : Exception() {
}