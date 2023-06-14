package com.example.baseprojectandroid.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.reflect.KClass

/**
 * Created by BuiTuyen
 */

class LocalData @Inject constructor(
    @ApplicationContext context: Context,
    @PreferenceInfo val fileName: String
) : LocalStorage {

    private val sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> putData(key: String, t: T?) {
        if (t != null) {
            val str = Gson().toJson(t)
            putString(key, str)
        } else putString(key, null)
    }

    override fun <T : Any> getData(key: String): T? {
        val string = getString(key) ?: return null
        try {
            return Gson().fromJson(string, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
        }
        return null
    }

    override fun <T : Any> getData(key: String, clazz: KClass<T>): T? {
        val string = getString(key) ?: return null
        try {
            return Gson().fromJson(string, clazz.java)
        } catch (e: Exception) {
        }
        return null
    }

    override fun putString(key: String, value: String?) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String): String {
        return sharedPreferences.getString(key, null) ?: return ""
    }

    override var username: String
        get() = getString("USER_NAME")
        set(value) {
            putString("USER_NAME", value)
        }

    override var nameUser: String
        get() = getString("NAME_USER")
        set(value) {
            putString("NAME_USER", value)
        }

    override var isFirstTime: Boolean
        get() = getData("IS_FIRST_TIME", Boolean::class) ?: true
        set(value) {
            putData("IS_FIRST_TIME", value)
        }

    override var password: String
        get() = getString("PASSWORD")
        set(value) {
            putString("PASSWORD", value)
        }

}
