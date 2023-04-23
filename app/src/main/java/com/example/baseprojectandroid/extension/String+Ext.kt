package com.example.baseprojectandroid.extension

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.MessageDigest
import kotlin.experimental.and

fun String.urlEncoder(): String {
    return try {
        URLEncoder.encode(this, "UTF-8").replace("+", "%20")
    } catch (e: UnsupportedEncodingException) {
        this
    }
}

inline fun <reified T> fromJson(json: String): T {
    return Gson().fromJson(json, object : TypeToken<T>() {}.type)
}

val String.fromHtml: Spanned
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(this)
        }

fun String.fromHex(): String {
    val str = StringBuilder()
    var i = 0
    while (i < length) {
        str.append(substring(i, i + 2).toInt(16).toChar())
        i += 2
    }
    return str.toString()
}

fun String?.isNullOrEmptyOrBlank(): Boolean {
    return isNullOrEmpty() || isNullOrBlank()
}

fun String.toHex(pass: String): String {
    val md = MessageDigest.getInstance("SHA-1")
    md.update(pass.toByteArray())
    val bytes = md.digest(this.toByteArray())
    val sb = StringBuilder()
    for (element in bytes) {
        sb.append(((element and 0xff.toByte()) + 0x100).toString(16).substring(1))
    }
    return sb.toString()
}

fun String.findStringOccurrencesAndIndex(stringToFind: String): ArrayList<Int> {
    val list = ArrayList<Int>()
    var index: Int = indexOf(stringToFind)
    while (index >= 0) {
        println(index)
        index = indexOf(stringToFind, index + 1)
        list.add(index)
    }
    return list
}

val Any.toJson: String
    get() {
        return try {
            Gson().toJson(this)
        } catch (e: Exception) {
            ""
        }
    }