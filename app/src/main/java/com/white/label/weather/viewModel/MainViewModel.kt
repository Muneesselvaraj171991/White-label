package com.white.label.weather.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.white.label.weather.network.RemoteCall
import com.google.gson.Gson
import com.white.label.weather.model.Weather
import com.white.label.weather.ui.label.UiCompose
import com.white.label.weather.ui.theme.BkgDrawablesRes
import com.white.label.weather.ui.theme.IconDrawables
import com.white.label.weather.ui.theme.darkH1TextSize
import com.white.label.weather.ui.theme.darkH2TextSize
import com.white.label.weather.ui.theme.darkNormalTextSize
import com.white.label.weather.ui.theme.darkPrimaryColor
import com.white.label.weather.ui.theme.darkSecondaryColor
import com.white.label.weather.ui.theme.darkTertiaryColor
import com.white.label.weather.ui.theme.darkTextColor
import com.white.label.weather.ui.theme.lightH1TextSize
import com.white.label.weather.ui.theme.lightH2TextSize
import com.white.label.weather.ui.theme.lightNormalTextSize
import com.white.label.weather.ui.theme.lightPrimaryColor
import com.white.label.weather.ui.theme.lightSecondaryColor
import com.white.label.weather.ui.theme.lightTertiaryColor
import com.white.label.weather.ui.theme.lightTextColor
import com.white.label.weather.util.AppUtil
import com.white.label.weather.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val mRemoteCall: RemoteCall = RemoteCall.remoteCallInstance
    val uiComposeFlow: MutableStateFlow<UiCompose?> = MutableStateFlow(null)
    var webApiFlowData: MutableStateFlow<Weather?> = MutableStateFlow(null)
    var appBgImageResourceFlow: MutableStateFlow<BkgDrawablesRes> =
        MutableStateFlow(BkgDrawablesRes()) // assigning default value to avoid drawing time
    var appIconImageResourceFlow: MutableStateFlow<IconDrawables> =
        MutableStateFlow(IconDrawables()) // assigning default value
    var currentWeatherCodeFlow: MutableStateFlow<Int> = MutableStateFlow(0)


    /**
     * Decided to parse JsonUI api in ViewModel, just to avoid re-parsing the same data on configuration change.
     */
    fun parseJsonUi(response: String) {
        viewModelScope.async(Dispatchers.IO) {
            try {
                val gson = Gson()
                val uiCompose = gson.fromJson(response, UiCompose::class.java)
                //parse the theme
                uiCompose.theme?.light.let {
                    val lightTheme = uiCompose!!.theme?.light
                    if (lightTheme != null) {
                        lightPrimaryColor = AppUtil.getColor(lightTheme.primary)
                        lightSecondaryColor = AppUtil.getColor(lightTheme.secondary)
                        lightTertiaryColor = AppUtil.getColor(lightTheme.tertiary)
                        lightTextColor = AppUtil.getColor(lightTheme.textColor)
                        lightH1TextSize = lightTheme.h1TextSize
                        lightH2TextSize = lightTheme.h2TextSize
                        lightNormalTextSize = lightTheme.normalTextSize

                    }
                }
                uiCompose.theme?.dark.let {

                    val darkTheme = uiCompose.theme?.dark
                    if (darkTheme != null) {
                        darkPrimaryColor = AppUtil.getColor(darkTheme.primary)
                        darkSecondaryColor = AppUtil.getColor(darkTheme.secondary)
                        darkTertiaryColor = AppUtil.getColor(darkTheme.tertiary)
                        darkTextColor = AppUtil.getColor(darkTheme.textColor)
                        darkH1TextSize = darkTheme.h1TextSize
                        darkH2TextSize = darkTheme.h2TextSize
                        darkNormalTextSize = darkTheme.normalTextSize

                    }
                }

                //parsing app background images
                val backgroundDrawables = uiCompose.mainScreen?.drawables?.bgImage
                backgroundDrawables?.let {

                    if (backgroundDrawables.drawableType!!.lowercase(Locale.ROOT) == Constants.IMG_TYPE_URL) {
                        appBgImageResourceFlow.value =
                            BkgDrawablesRes(
                                type = Constants.IMG_TYPE_URL,
                                bgImgUrlResponse = backgroundDrawables
                            )


                    } else if (backgroundDrawables.drawableType.lowercase(Locale.ROOT) == Constants.IMG_TYPE_DRAWABLE) {
                        appBgImageResourceFlow.value =
                            BkgDrawablesRes(
                                type = Constants.IMG_TYPE_DRAWABLE,
                                bgRaining = AppUtil.getImageId(backgroundDrawables.rainImageSrc!!),
                                bgCloud = AppUtil.getImageId(backgroundDrawables.cloudImageSrc!!),
                                bgSnowing = AppUtil.getImageId(backgroundDrawables.snowImageSrc!!),
                                bgSunny = AppUtil.getImageId(backgroundDrawables.sunnyImageSrc!!),
                                bgThundar = AppUtil.getImageId(backgroundDrawables.thunderImageSrc!!)
                            )


                    } else {
                        throw IllegalArgumentException("Drawable type should be either drawable||url")
                    }
                }


                //parsing app icons images
                val iconDrawables = uiCompose.mainScreen?.drawables?.icons
                iconDrawables?.let {
                    if (iconDrawables.drawableType!!.lowercase(Locale.ROOT) == Constants.IMG_TYPE_URL) {
                        appIconImageResourceFlow.value =
                            IconDrawables(
                                type = Constants.IMG_TYPE_URL,
                                iconImgUrlResponse = iconDrawables
                            )


                    } else if (iconDrawables.drawableType.lowercase(Locale.ROOT) == Constants.IMG_TYPE_DRAWABLE) {
                        appIconImageResourceFlow.value =
                            IconDrawables(
                                type = Constants.IMG_TYPE_DRAWABLE,
                                iconRaining = AppUtil.getImageId(iconDrawables.rainImageSrc!!),
                                iconCloud = AppUtil.getImageId(iconDrawables.cloudImageSrc!!),
                                iconSnowing = AppUtil.getImageId(iconDrawables.snowImageSrc!!),
                                iconSunny = AppUtil.getImageId(iconDrawables.sunnyImageSrc!!),
                                iconThundar = AppUtil.getImageId(iconDrawables.thunderImageSrc!!)
                            )


                    } else {
                        throw IllegalArgumentException("Drawable type should be either drawable||url")
                    }
                }

                uiComposeFlow.value = uiCompose

            } catch (th: Throwable) {
                Log.d("Exception is caught while parsing Json api", th.toString())
            }

        }

    }

    fun fetWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.async(Dispatchers.IO) {

            mRemoteCall.weatherApi(object : RemoteCall.Result {
                override fun onResponse(weather: Weather) {
                    currentWeatherCodeFlow.value = weather.current_weather.weathercode
                    AppUtil.getCityName(weather.latitude, weather.longitude) {
                        if (it != null) {
                            weather.location = it
                        }
                    }
                    webApiFlowData.value = weather
                }

                override fun onFailure() {

                }

            }, latitude, longitude)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}