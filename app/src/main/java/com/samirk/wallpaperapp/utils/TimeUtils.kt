package com.samirk.wallpaperapp.utils

import java.util.*

/**
 *  Date and Time Utils
 */

fun getCurrentTimeMillis() = Date().time

class TimeUtils {

    companion object {

        //TODO make this correct for day
        private const val MILLIS_IN_A_DAY = 1000 * 60
    }

    /**
     * Check if millis are of today date
     */
    fun isToday(millis: Long): Boolean {
        val today = getCurrentTimeMillis()

        //number of days difference between 2 dates
        val days: Int = ((today - millis) / MILLIS_IN_A_DAY).toInt()

        return days < 1
    }
}