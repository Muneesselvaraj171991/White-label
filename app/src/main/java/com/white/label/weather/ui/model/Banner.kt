package com.white.label.weather.ui.model

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
    val backgroundColor: Color =
        if (bgColor == "Transparent") Color.Transparent else Color(bgColor.toColorInt())

    fun getBannerUnit(): BannerUnit {
        if (type.isEmpty() && !isValidBannerType(type)) throw IllegalArgumentException("Banner type should not be empty") else {

            val bannerWeight: Float = when {
                (type == BANNER_TYPE_PREDICTION_LIST) -> 2f
                (type == BANNER_TYPE_DAY_PREDICTION) -> 1.20f
                (type == BANNER_TYPE_CURRENT_WEATHER) -> .80f
                else -> {
                    0f
                }
            }
            return BannerUnit(type, bannerWeight)
        }
    }


    private fun isValidBannerType(type: String): Boolean {
        return (type == BANNER_TYPE_CURRENT_WEATHER || type == BANNER_TYPE_DAY_PREDICTION || type == BANNER_TYPE_PREDICTION_LIST)
    }
}