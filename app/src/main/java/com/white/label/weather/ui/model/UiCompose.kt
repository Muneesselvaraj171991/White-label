package com.white.label.weather.ui.model

data class UiCompose(
    val mainScreen: MainScreen=  MainScreen(Drawables(), banners = listOf(Banner(type = "current_weather"), Banner(type = "day_prediction"), Banner(type = "prediction_list"))),
    val theme: Theme = Theme(Dark(), Light())
)
