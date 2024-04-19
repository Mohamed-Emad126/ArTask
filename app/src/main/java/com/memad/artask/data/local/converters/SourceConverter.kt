package com.memad.artask.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.memad.artask.data.remote.models.Source

class SourceConverter {

    @TypeConverter
    fun fromSource(source: Source?): String {
        return Gson().toJson(source!!)
    }

    @TypeConverter
    fun toSource(name: String): Source? {
        return Gson().fromJson(name, Source::class.java)
    }
}