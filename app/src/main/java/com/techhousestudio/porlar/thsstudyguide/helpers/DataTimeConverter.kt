package com.techhousestudio.porlar.thsstudyguide.helpers

import org.threeten.bp.LocalTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


object Converters {

    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    fun fromTimeString(startTime: String?, endTime: String?): String {
        Timber.i("%s%s", startTime, endTime)
        val hour = LocalTime.parse(startTime?.substring(0,startTime.length-3)).hour.minus(LocalTime.parse(endTime?.substring(0,endTime.length-3)).hour)
        val minutes = LocalTime.parse(startTime?.substring(0,startTime.length-3)).minute.minus(LocalTime.parse(endTime?.substring(0,endTime.length-3)).minute)

        return "${abs(hour)} h ${abs(minutes)} m"
    }

}