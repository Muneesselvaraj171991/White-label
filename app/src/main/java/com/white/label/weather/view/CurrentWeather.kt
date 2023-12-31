package com.white.label.weather.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.white.label.weather.ui.theme.h1TextSize
import com.white.label.weather.ui.theme.h2TextSize
import com.white.label.weather.ui.theme.normalTextSize
import com.white.label.weather.ui.theme.textColor
import com.white.label.weather.util.AppUtil
import com.white.label.weather.viewModel.MainViewModel


@Composable
fun CurrentWeatherScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        val weatherApi by viewModel.webApiLiveData.observeAsState()
        weatherApi?.let {it
            Text(
                text = AppUtil.getCityName(it.latitude, it.longitude),
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

