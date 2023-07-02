package com.example.baseprojectandroid.local

import com.example.baseprojectandroid.model.User
import kotlin.reflect.KClass

interface LocalStorage {

    fun putString(key: String, value: String?)
    fun getString(key: String): String?
    fun remove(key: String)

    fun <T : Any> putData(key: String, t: T?)
    fun <T : Any> getData(key: String): T?
    fun <T : Any> getData(key: String, clazz: KClass<T>): T?

    var username: String
    var password: String
    var nameUser: String
    var isFirstTime: Boolean

    var currentUser: User?
    var allUsers: List<User>
}