package com.white.label.weather.ui.label

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.white.label.weather.util.Constants.Companion.BANNER_TYPE_CURRENT_WEATHER
import com.white.label.weather.util.Constants.Companion.BANNER_TYPE_DAY_PREDICTION
import com.white.label.weather.util.Constants.Companion.BANNER_TYPE_PREDICTION_LIST

data class Banner(
    private val bgColor: String = "Transparent",
    private val type: String = "",
    val title: String? = ""
) {

    fun getBannerUnit(): BannerUnit {
        if (type.isEmpty() && !isValidBannerType(type)) throw IllegalArgumentException("Banner type should not be empty") else {
            val backgroundColor: Color =
                if (bgColor == "Transparent") Color.LightGray else Color(bgColor.toColorInt())

            val bannerWeight: Float = when {
                (type == BANNER_TYPE_PREDICTION_LIST) -> 2f
                (type == BANNER_TYPE_DAY_PREDICTION) -> 1.20f
                (type == BANNER_TYPE_CURRENT_WEATHER) -> .80f
                else -> {
                    0f
                }
            }
            return BannerUnit(type, bannerWeight, backgroundColor)
        }
    }


    private fun isValidBannerType(type: String): Boolean {
        return (type == BANNER_TYPE_CURRENT_WEATHER || type == BANNER_TYPE_DAY_PREDICTION || type == BANNER_TYPE_PREDICTION_LIST)
    }
}