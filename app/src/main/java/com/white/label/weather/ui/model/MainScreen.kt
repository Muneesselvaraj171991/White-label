package com.white.label.weather.ui.model

data class MainScreen(
    val drawables: Drawables? = Drawables(),
    val banners: List<Banner>? = listOf(
        Banner(type = "current_weather"),
        Banner(type = "day_prediction"),
        Banner(type = "prediction_list")
    ),

    )
