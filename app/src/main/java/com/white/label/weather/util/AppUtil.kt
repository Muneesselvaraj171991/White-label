package com.white.label.weather.util

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt


class AppUtil {
    companion object Factory {

        fun getWeatherStatus(code: Int): String {
            /* 0	Clear sky
        1, 2, 3	Mainly clear, partly cloudy, and overcast
        45, 48	Fog and depositing rime fog
        51, 53, 55	Drizzle: Light, moderate, and dense intensity
        56, 57	Freezing Drizzle: Light and dense intensity
        61, 63, 65	Rain: Slight, moderate and heavy intensity
        66, 67	Freezing Rain: Light and heavy intensity
        71, 73, 75	Snow fall: Slight, moderate, and heavy intensity
        77	Snow grains
        80, 81, 82	Rain showers: Slight, moderate, and violent
        85, 86	Snow showers slight and heavy
        95 *	Thunderstorm: Slight or moderate
        96, 99 *	Thunderstorm with slight and heavy hail*/
            if ((code <= 0)) {
                return "Clear Sky"

            } else if (code <= 3) {
                return "Mainly clear"
            } else if (code <= 48) {
                return "Fog"
            } else if (code <= 55) {
                return "Drizzle"
            } else if (code <= 57) {
                return "Freezing Drizzle"
            } else if (code <= 65) {
                return "Rain"

            } else if (code <= 67) {
                return "Freezing Rain"
            } else if (code <= 75) {
                return "Snow fall"
            } else if (code <= 77) {
                return " Snow grains"
            } else if (code <= 82) {
                return "Rain showers"
            } else if (code <= 86) {
                return "Snow showers"
            } else if (code <= 95) {
                return "Thunderstorm"
            } else if (code > 95) {
                return "Thunderstorm with slight and heavy hail"
            }
            return "NA"
        }
        fun getColor(myColorString: String): Color {
            try {
                if(myColorString.isEmpty()) {
                    return Color(myColorString.toColorInt())
                } else {
                    throw  IllegalArgumentException("Invalid color code, App needs valid color code");

                }
            } catch (ex: Exception) {
                throw  IllegalArgumentException("Invalid color code, App needs valid color code");
            }
        }

        fun getImageId(context: Context, imageName: String): Int {
            try {
                return context.resources.getIdentifier(
                    "drawable/$imageName",
                    null,
                    context.packageName
                )
            } catch (ex: IllegalArgumentException) {
                throw IllegalArgumentException("Passing drawable name:$imageName is not valid")
            }
        }
    }
}