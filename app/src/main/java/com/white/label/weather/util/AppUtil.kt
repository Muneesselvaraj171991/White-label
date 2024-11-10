package com.white.label.weather.util

import android.location.Address
import android.location.Geocoder
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.white.label.weather.MainApplication
import com.white.label.weather.ui.label.BgImage
import com.white.label.weather.ui.label.Icons
import com.white.label.weather.ui.theme.BkgDrawablesRes
import com.white.label.weather.ui.theme.IconDrawables
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


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

        fun getBgImageUrl(code: Int, bgResource: BkgDrawablesRes): Int {
            if (code == 0) {
                return bgResource.bgSunny
            } else if (code <= 48) {
                return bgResource.bgCloud
            } else if (code <= 67) {
                return bgResource.bgRaining

            } else if (code <= 86) {
                return bgResource.bgSnowing
            } else if (code >= 87) {
                return bgResource.bgThundar
            }
            return bgResource.bgSunny
        }

        fun getBgImageUrl(code: Int, bgUrl: BgImage): String? {
             if (code == 0) {
                return bgUrl.sunnyImageSrc
            } else if (code <= 48) {
                return bgUrl.cloudImageSrc
            } else if (code <= 67) {
                return bgUrl.rainImageSrc

            } else if (code <= 86) {
                return bgUrl.snowImageSrc
            } else if (code >= 87) {

                return bgUrl.thunderImageSrc
            }
            return bgUrl.sunnyImageSrc
        }

        fun getIconImageResource(code: Int, iconResource: IconDrawables): Int {
            if (code <= 3) {
                return iconResource.iconSunny
            } else if (code <= 48) {
                return iconResource.iconCloud
            } else if (code <= 67) {
                return iconResource.iconRaining

            } else if (code <= 86) {
                return iconResource.iconSnowing
            } else if (code >= 87) {

                return iconResource.iconThundar
            }
            return iconResource.iconSunny
        }

        fun getIconImageUrl(code: Int, iconUrl: Icons): String? {
            if (code <= 3) {
                return iconUrl.sunnyImageSrc
            } else if (code <= 48) {
                return iconUrl.cloudImageSrc
            } else if (code <= 67) {
                return iconUrl.rainImageSrc

            } else if (code <= 86) {
                return iconUrl.snowImageSrc
            } else if (code >= 87) {

                return iconUrl.thunderImageSrc
            }
            return iconUrl.sunnyImageSrc
        }

        @Throws(IllegalArgumentException::class)
        fun getColor(myColorString: String): Color {
            if (myColorString.isNotEmpty()) {
                return Color(myColorString.toColorInt())
            } else {
                throw IllegalArgumentException("Invalid color code, App needs valid color code")

            }
        }

        @Throws(IllegalArgumentException::class)
        fun getImageId(imageName: String): Int {
            if (imageName.isNotEmpty()) {
                return MainApplication.appContext.resources.getIdentifier(
                    "drawable/$imageName",
                    null,
                    MainApplication.appContext.packageName
                )
            } else {
                throw IllegalArgumentException("Image resource should not be empty")

            }
        }

        fun getDay(dateStr: String): String {
            val date: Date
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            date = formatter.parse(dateStr)
            val c: Calendar = Calendar.getInstance()
            c.time = date
            val dayOfWeek: Int = c.get(Calendar.DAY_OF_WEEK)
            when (dayOfWeek) {
                Calendar.SUNDAY -> {
                    return "Sunday"
                }

                Calendar.MONDAY -> {
                    return "Monday"
                }

                Calendar.TUESDAY -> {
                    return "Tuesday"
                }

                Calendar.WEDNESDAY -> {
                    return "Wednesday"
                }

                Calendar.THURSDAY -> {
                    return "Thursday"
                }

                Calendar.FRIDAY -> {
                    return "Friday"
                }

                Calendar.SATURDAY -> {
                    return "Saturday"
                }
            }
            return "NA"
        }

        fun getCityName(lat: Double, long: Double,  callback: (String?)->Unit) {
            var cityName: String?
            val geoCoder = Geocoder(MainApplication.appContext, Locale.getDefault())
            geoCoder.getFromLocation(lat, long, 3, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    cityName = addresses[0].adminArea
                    callback(cityName)
                    // code
                }

                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                    callback("Unable to fetch loc!")


                }

            })

        }
    }
}