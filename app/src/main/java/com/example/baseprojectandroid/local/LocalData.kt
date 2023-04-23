package com.example.baseprojectandroid.local

import android.content.Context
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

    override fun putString(key: String, value: String?) {
        TODO("Not yet implemented")
    }

    override fun getString(key: String): String? {
        TODO("Not yet implemented")
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> putData(key: String, t: T?) {
        TODO("Not yet implemented")
    }

    override fun <T : Any> getData(key: String): T? {
        TODO("Not yet implemented")
    }

    override fun <T : Any> getData(key: String, clazz: KClass<T>): T? {
        TODO("Not yet implemented")
    }

}
