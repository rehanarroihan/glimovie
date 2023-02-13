package com.multazamgsd.glimovie.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object GeneralHelpers {
    const val DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    @SuppressLint("SimpleDateFormat")
    fun String.toTimeFormat(toFormat:String = "dd MMMM yyyy - HH:mm"): String {
        val idf = SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT, Locale("IND","ID")).parse(this)
        return SimpleDateFormat(toFormat).format(idf!!)
    }
}