package com.white.label.weather.ui.model

import com.white.label.weather.ui.theme.darkPrimaryColor
import com.white.label.weather.ui.theme.darkSecondaryColor
import com.white.label.weather.ui.theme.darkTertiaryColor
import com.white.label.weather.ui.theme.darkTextColor
import com.white.label.weather.util.AppUtil

data class Dark(
    val h1TextSize: Int = 24,
    val h2TextSize: Int = 22,
    val normalTextSize: Int = 16,
    private val primary: String? = null,
    private val secondary: String? = null,
    private val tertiary: String? = null,
    private val textColor: String? = null
) {
    val primaryColor = if (primary == null) darkPrimaryColor else AppUtil.getColor(primary)
    val secondaryColor = if (secondary == null) darkSecondaryColor else AppUtil.getColor(
        secondary
    )
    val tertiaryColor = if (tertiary == null) darkTertiaryColor else AppUtil.getColor(tertiary)
    val themeTextColor = if (textColor == null) darkTextColor else AppUtil.getColor(textColor)


}
