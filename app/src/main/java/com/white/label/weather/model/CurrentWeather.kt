package com.white.label.weather.model

data class CurrentWeather(
    val is_day: Int,
    val temperature: Double,
    val time: String,
    val weathercode: Int,
    val winddirection: Int,
    val windspeed: Double
)