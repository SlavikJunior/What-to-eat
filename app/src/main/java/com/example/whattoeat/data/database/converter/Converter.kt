package com.example.whattoeat.data.database.converter

import androidx.room.TypeConverter
import java.sql.Timestamp


class Converter {

    @TypeConverter
    fun fromLongToTimesTamp(value: Long?) = value?.let { Timestamp(it) }

    @TypeConverter
    fun fromTimesTampToLong(value: Timestamp?) = value?.time
}