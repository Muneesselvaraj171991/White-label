package com.white.label.weather.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class Hourly(
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weathercode: List<Int>

) {
    fun getHourlyPredictions(): List<DayPredictionHourly> {
        val predictionHourly = ArrayList<DayPredictionHourly>(24)
        for (index in 0 until 24) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
            val dateTime = LocalDateTime.parse(time[index], formatter)
            predictionHourly.add(
                DayPredictionHourly(
                    String.format(
                        "%02d",
                        dateTime.hour
                    ) + ":" + String.format("%02d", dateTime.minute),
                    weathercode[index],
                    temperature_2m[index]
                )
            )
        }
        return predictionHourly
    }
}