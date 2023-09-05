package com.white.label.weather.ui.model

import com.white.label.weather.ui.theme.lightPrimaryColor
import com.white.label.weather.ui.theme.lightSecondaryColor
import com.white.label.weather.ui.theme.lightTertiaryColor
import com.white.label.weather.ui.theme.lightTextColor
import com.white.label.weather.util.AppUtil.Factory.getColor


data class Light(
     val h1TextSize: Int = 24,
     val h2TextSize: Int = 22,
     val normalTextSize: Int = 16,
     val primary: String="",
     val secondary: String="",
     val tertiary: String="",
     val textColor: String=""
)

