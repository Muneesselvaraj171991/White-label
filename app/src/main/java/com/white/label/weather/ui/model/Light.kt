package com.white.label.weather.ui.model

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.white.label.weather.ui.theme.lightPrimaryColor
import com.white.label.weather.ui.theme.lightSecondaryColor
import com.white.label.weather.ui.theme.lightTertiaryColor
import com.white.label.weather.ui.theme.lightTextColor
import com.white.label.weather.util.AppUtil.Factory.getColor


data class Light(
    val h1TextSize: Int = 24,
    val h2TextSize: Int = 22,
    val normalTextSize: Int = 16,
    private val primary: String? = null ,
    private val secondary: String? = null,
    private val tertiary: String? = null,
    private val textColor: String? = null
) {
    val primaryColor = if(primary == null) lightPrimaryColor else getColor(primary)
    val secondaryColor = if(secondary == null) lightSecondaryColor else getColor(secondary)
    val tertiaryColor = if(tertiary == null) lightTertiaryColor else getColor(tertiary)
    val themeTextColor = if(textColor== null) lightTextColor else getColor(textColor)


}

