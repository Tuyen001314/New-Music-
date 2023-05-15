package com.example.baseprojectandroid.model

data class Song(
    val name: String,
    val url: String
) {
    companion object {
        val EMPTY = Song("", "")
    }
}