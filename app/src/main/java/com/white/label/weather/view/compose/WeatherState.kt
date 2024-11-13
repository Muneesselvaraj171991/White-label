package com.white.label.weather.view.compose

import com.white.label.weather.model.Weather

data class WeatherState(
    var weatherInfo: Weather? = null,
    var isLoading: Boolean = false,
    val error: String? = null
)
