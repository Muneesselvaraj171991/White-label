package com.white.label.weather.view.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.white.label.weather.model.Weather
import com.white.label.weather.view.ui.label.UiCompose
import com.white.label.weather.view.ui.theme.darkH1TextSize
import com.white.label.weather.view.ui.theme.darkH2TextSize
import com.white.label.weather.view.ui.theme.darkNormalTextSize
import com.white.label.weather.view.ui.theme.darkPrimaryColor
import com.white.label.weather.view.ui.theme.darkTextColor
import com.white.label.weather.view.ui.theme.h1TextSize
import com.white.label.weather.view.ui.theme.h2TextSize
import com.white.label.weather.view.ui.theme.lightH1TextSize
import com.white.label.weather.view.ui.theme.lightH2TextSize
import com.white.label.weather.view.ui.theme.lightNormalTextSize
import com.white.label.weather.view.ui.theme.lightPrimaryColor
import com.white.label.weather.view.ui.theme.lightTextColor
import com.white.label.weather.view.ui.theme.normalTextSize
import com.white.label.weather.view.ui.theme.textColor
import com.white.label.weather.util.AppUtil
import com.white.label.weather.util.Constants
import com.white.label.weather.util.Constants.Companion.IMG_TYPE_DRAWABLE
import com.white.label.weather.viewModel.MainViewModel

@Composable
fun MainScreen(weather: Weather?, viewModel: MainViewModel, uiCompose: UiCompose?) {
    var color: Color?
    val bgImage by viewModel.appBgImageResourceFlow.collectAsStateWithLifecycle()
    val currentWeatherCode by viewModel.currentWeatherCodeFlow.collectAsStateWithLifecycle()
    weather?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    if (bgImage.type == IMG_TYPE_DRAWABLE) painterResource(
                        id = AppUtil.getBgImageUrl(
                            currentWeatherCode,
                            bgImage
                        )
                    ) else rememberAsyncImagePainter(
                        AppUtil.getBgImageUrl(
                            currentWeatherCode,
                            bgImage.bgImgUrlResponse!!
                        )
                    ),
                    contentScale = ContentScale.FillBounds
                )
        ) {

            if (isSystemInDarkTheme()) {
                h1TextSize = darkH1TextSize.sp
                h2TextSize = darkH2TextSize.sp
                normalTextSize = darkNormalTextSize.sp
                textColor = darkTextColor
                color = darkPrimaryColor

            } else {
                h1TextSize = lightH1TextSize.sp
                h2TextSize = lightH2TextSize.sp
                normalTextSize = lightNormalTextSize.sp
                textColor = lightTextColor
                color = lightPrimaryColor
            }

            for (banner in uiCompose?.mainScreen?.banners!!) {
                val bannerUnit = banner.getBannerUnit()
                Row(modifier = Modifier.weight(bannerUnit.weight, true)) {
                    when (bannerUnit.type) {
                        Constants.BANNER_TYPE_CURRENT_WEATHER -> CurrentWeatherScreen(weather)

                        Constants.BANNER_TYPE_DAY_PREDICTION -> DayPredictionScreen(
                            viewModel, weather,
                            color,
                            banner.title
                        )

                        Constants.BANNER_TYPE_PREDICTION_LIST -> DaysPredictionList(
                            viewModel, weather,
                            color,
                            banner.title
                        )
                    }
                }
            }
        }

    }
}
