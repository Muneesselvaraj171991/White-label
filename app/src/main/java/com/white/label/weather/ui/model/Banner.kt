package com.white.label.weather.ui.model

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import java.lang.IllegalArgumentException

data class Banner(
   private val bgColor: String = "Transparent",
  private  val type: String= ""
) {
    val backgroundColor: Color = if(bgColor == "Transparent") Color.Transparent else Color(bgColor.toColorInt())
    fun getBannerUnit(): BannerUnit {
        if (type.isEmpty() && !isValidBannerType(type)) throw IllegalArgumentException("Banner type should not be empty") else {
            val banner: String = type
            val bannerWeight: Float = if (type == "prediction_list") 2f else 1f
           return BannerUnit(type, bannerWeight)
        }
    }


    private fun isValidBannerType(type: String): Boolean {
        return (type=="current_weather" || type== "day_prediction" || type == "prediction_list")
    }
}