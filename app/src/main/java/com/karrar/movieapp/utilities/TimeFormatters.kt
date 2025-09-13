package com.karrar.movieapp.ui.profile.myratings

import java.text.SimpleDateFormat
import java.util.Locale

object TimeFormatters {
    fun formatMinutes(total: Int): String {
        if (total <= 0) return ""
        val h = total / 60
        val m = total % 60
        return buildString {
            if (h > 0) append("${h}h ")
            if (m > 0) append("${m}m")
        }.trim()
    }

    private val inputDate by lazy { SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH) }
    private val outputDate by lazy { SimpleDateFormat("yyyy, MMM dd", Locale.ENGLISH) }

    fun formatDate(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        return try {
            val d = inputDate.parse(raw)
            outputDate.format(d!!)
        } catch (_: Exception) {
            raw
        }
    }
}