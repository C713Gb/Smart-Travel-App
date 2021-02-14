package com.example.smarttravel.Utils

import java.util.*

class Common {
    companion object{

        //common fun for greeting message
        fun getGreetingMessage(): String {
            val c = Calendar.getInstance()
            val timeOfDay = c.get(Calendar.HOUR_OF_DAY)
            return when (timeOfDay) {
                in 0..11 -> "Good Morning"
                in 12..15 -> "Good Afternoon"
                in 16..20 -> "Good Evening"
                in 21..23 -> "Good Night"
                else -> {
                    "Hello"
                }
            }
        }

    }
}