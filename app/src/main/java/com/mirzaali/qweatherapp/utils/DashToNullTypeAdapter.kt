package com.mirzaali.qweatherapp.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class DashToNullTypeAdapter : TypeAdapter<Int>() {
    override fun write(out: JsonWriter, value: Int?) {
        out.value(value)
    }

    override fun read(reader: JsonReader): Int? {
        return when (val value = reader.nextString()) {
            "-" -> null
            else -> value.toIntOrNull()
        }
    }
}