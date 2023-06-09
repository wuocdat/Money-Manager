package com.wuocdat.moneymanager.Utils

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class TimeUtils {

    companion object {

        fun timeFormat(timestamp: Long, format: String = "dd/MM/yyyy"): String {
            val date = Date(timestamp)  //millisecond
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())

            return dateFormat.format(date)
        }

        fun timeFormat(
            timeString: String,
            inputPattern: String = "yyyy-M",
            outputPattern: String = "MMMM yyyy"
        ): String {
            val inputFormatter = SimpleDateFormat(inputPattern, Locale.getDefault())
            val outputFormatter = SimpleDateFormat(outputPattern, Locale.getDefault())

            val date = inputFormatter.parse(timeString)
            return date?.let { outputFormatter.format(it) } ?: ""
        }

        fun startOfDayTimestamp(timestamp: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.timeInMillis
        }

        fun endOfDayTimestamp(timestamp: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            calendar.set(Calendar.HOUR_OF_DAY, 24)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.timeInMillis
        }


        fun getCurrentMonth(): String {
            val calendar = Calendar.getInstance()
            return String.format("%02d", (calendar.get(Calendar.MONTH) + 1))
        }

        fun getCurrentYear(): String {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.YEAR).toString()
        }

        fun getMonthAndYearStr(): String = "${getCurrentYear()}-${getCurrentMonth()}"

        fun getMonthAndYearStr(month: Int, year: Int): String =
            "$year-${String.format("%02d", month)}"

        fun textToTimestamp(text: String, pattern: String = "dd/MM/yyyy"): Long {
            val format = SimpleDateFormat(pattern, Locale.getDefault())
            val date = format.parse(text)
            return date?.time ?: 0L
        }

        fun isDateFormat(text: String): Boolean {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            format.isLenient = false
            return try {
                val date = format.parse(text)
                date != null
            } catch (e: Exception) {
                false
            }
        }

    }

}