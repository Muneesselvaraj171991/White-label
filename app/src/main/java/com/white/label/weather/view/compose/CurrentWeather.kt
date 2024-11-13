package com.white.label.weather.view.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.white.label.weather.model.Weather
import com.white.label.weather.view.ui.theme.h1TextSize
import com.white.label.weather.view.ui.theme.h2TextSize
import com.white.label.weather.view.ui.theme.normalTextSize
import com.white.label.weather.view.ui.theme.textColor
import com.white.label.weather.util.AppUtil


@Composable
fun CurrentWeatherScreen(weather: Weather?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
       weather?.let {
            Text(
                text = it.location,
                fontSize = h2TextSize,
                color = textColor
            )
            Text(
                text = it.current_weather.temperature.toString() + "\u2103",
                fontSize = h1TextSize,
                color = textColor

            )
            Text(
                text = AppUtil.getWeatherStatus(it.current_weather.weathercode),
                fontSize = normalTextSize,
                color = textColor
            )
            Text(
                text = "H:${it.daily.temperature_2m_max[0]} L:${it.daily.temperature_2m_min[0]}",
                fontSize = normalTextSize,
                color = textColor
            )
        }

    }
}

