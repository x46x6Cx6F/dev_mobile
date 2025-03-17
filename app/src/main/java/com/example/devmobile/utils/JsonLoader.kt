package com.example.devmobile.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.devmobile.library.data.Trail
import java.io.InputStream
import java.lang.reflect.Type

fun loadTrailsFromJson(context: Context): List<Trail> {
    val inputStream: InputStream = context.assets.open("trails.json")
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()

    val json = String(buffer, Charsets.UTF_8)
    val type: Type = object : TypeToken<List<Trail>>() {}.type
    return Gson().fromJson(json, type)
}
