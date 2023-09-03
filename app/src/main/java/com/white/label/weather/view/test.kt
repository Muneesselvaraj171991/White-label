package com.white.label.weather.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun test() {
    Column {
        Row(modifier = Modifier.weight(1.0f, true)) {
            currentWeatherScreen()
        }
        Row(modifier = Modifier.weight(2.0f, true)) {
            dayPredictionScreen()
        }
        Row(modifier = Modifier.weight(2.0f, true)) {
           daysPredictionList()
        }
    }
}

@Preview
@Composable
fun test1() {
    test()
}