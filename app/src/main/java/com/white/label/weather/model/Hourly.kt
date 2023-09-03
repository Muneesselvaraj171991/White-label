package com.white.label.weather.model

data class Hourly(
    val temperature_2m: List<Double>,
    val time: List<String>
)