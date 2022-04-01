package com.kvr.user.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getFormattedDate(calendar: Calendar): String{
        val monthStr:String = when(calendar.get((Calendar.MONTH))){
            0 -> "Jan"
            1 -> "Feb"
            2 -> "Mar"
            3 -> "Apr"
            4 -> "May"
            5 -> "Jun"
            6 -> "Jul"
            7 -> "Aug"
            8 -> "Sep"
            9 -> "Oct"
            10 -> "Nov"
            11 -> "Dec"
            else -> "Unknown"
        }
        val year:Int = when(calendar.get(Calendar.YEAR)){
            2021 -> 21
            2022 -> 22
            2023 -> 23
            2024 -> 24
            2025 -> 25
            2026 -> 26
            2027 -> 27
            2028 -> 28
            2029 -> 29
            2030 -> 30
            2031 -> 31
            2032 -> 32
            2033 -> 33
            else -> calendar.get(Calendar.YEAR)
        }
        val date = "${calendar.get(Calendar.DATE)} $monthStr $year";
        return  date;
    }

    fun getTime(calendar: Calendar): String {
        return "${calendar.get(Calendar.HOUR_OF_DAY)}: ${calendar.get(Calendar.MINUTE)}"
    }



    fun formatDate(date:String) : String{
        val sourceSplit = date.split("T")
        val dateSplit = sourceSplit[0].split("-")
        val parseSplit = sourceSplit[1].split(".")
        val timeSplit = parseSplit[0].split(":")
        val calendar = Calendar.getInstance()
        calendar.set(dateSplit[0].toInt(),dateSplit[1].toInt()-1,dateSplit[2].toInt(), timeSplit[0].toInt(),timeSplit[1].toInt(),timeSplit[2].toInt())
        return SimpleDateFormat("dd-MM-yyyy HH:mm").format(calendar.time)
    }


    fun getMonth(month: Int): String {
        return when (month) {
            0 -> "January"
            1 -> "February"
            2 -> "March"
            3 -> "April"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "August"
            8 -> "September"
            9 -> "October"
            10 -> "November"
            11 -> "December"
            else -> "Unknown"
        }
    }

    fun isLettersOnly(value:String): Boolean {
        val len = value.length
        for (i in 0 until len) {
            if (!value[i].isLetter()) {
                return false
            }
        }
        return true
    }


}