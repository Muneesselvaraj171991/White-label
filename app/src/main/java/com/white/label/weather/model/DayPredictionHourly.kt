package com.white.label.weather.model

data class DayPredictionHourly(
    val time: String,
    val weatherCode: Int = -1,
    val temp: Double,
    val imageUrl: String = ""
)
