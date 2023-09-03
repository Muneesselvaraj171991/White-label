package com.example.white_label.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.white.label.weather.ui.model.Theme


var  textColor: Color = Color.White
var  h1TextSize: TextUnit = 24.sp
var h2TextSize: TextUnit = 22.sp
var normalTextSize: TextUnit = 16.sp


private fun darkColor(theme: Theme) : ColorScheme {
    theme.dark?.let {
        textColor = theme.dark.themeTextColor
        h1TextSize = theme.dark.h1TextSize.sp
        h2TextSize = theme.dark.h2TextSize.sp
        normalTextSize = theme.dark.normalTextSize.sp
        return darkColorScheme(
            primary = theme.dark.primaryColor,
            secondary = theme.dark.secondaryColor,
            tertiary = theme.dark.tertiaryColor

        )
    }

    return darkColorScheme()
}

private fun lightColor(theme: Theme) : ColorScheme {
    theme.light?.let {
        textColor = theme.light.themeTextColor
        h1TextSize = theme.light.h1TextSize.sp
        h2TextSize = theme.light.h2TextSize.sp
        normalTextSize = theme.light.normalTextSize.sp

        return lightColorScheme(
            primary = theme.light.primaryColor,
            secondary = theme.light.secondaryColor,
            tertiary = theme.light.tertiaryColor
        )
    }
    return lightColorScheme()
}

@Composable
fun WhitelabelTheme(themColor: Theme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if(darkTheme) darkColor(themColor)
        else lightColor(themColor)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}