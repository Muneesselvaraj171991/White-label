package com.white.label.weather.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.white_label.R
import com.white.label.weather.respository.RemoteCall
import com.google.gson.Gson
import com.white.label.weather.model.Weather
import com.white.label.weather.view.ui.label.UiCompose
import com.white.label.weather.view.ui.theme.BkgDrawablesRes
import com.white.label.weather.view.ui.theme.IconDrawables
import com.white.label.weather.view.ui.theme.darkH1TextSize
import com.white.label.weather.view.ui.theme.darkH2TextSize
import com.white.label.weather.view.ui.theme.darkNormalTextSize
import com.white.label.weather.view.ui.theme.darkPrimaryColor
import com.white.label.weather.view.ui.theme.darkSecondaryColor
import com.white.label.weather.view.ui.theme.darkTertiaryColor
import com.white.label.weather.view.ui.theme.darkTextColor
import com.white.label.weather.view.ui.theme.lightH1TextSize
import com.white.label.weather.view.ui.theme.lightH2TextSize
import com.white.label.weather.view.ui.theme.lightNormalTextSize
import com.white.label.weather.view.ui.theme.lightPrimaryColor
import com.white.label.weather.view.ui.theme.lightSecondaryColor
import com.white.label.weather.view.ui.theme.lightTertiaryColor
import com.white.label.weather.view.ui.theme.lightTextColor
import com.white.label.weather.util.AppUtil
import com.white.label.weather.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val mRemoteCall: RemoteCall = RemoteCall.remoteCallInstance
    private val uiComposeFlow: MutableStateFlow<UiCompose?> = MutableStateFlow(null)
    val mUiComposeFlow = uiComposeFlow.asStateFlow()
    private val webApiFlowData: MutableStateFlow<Weather?> = MutableStateFlow(null)
    val mWebApiFlowData = webApiFlowData.asStateFlow()
    private val appBgImageResourceFlow: MutableStateFlow<BkgDrawablesRes> =
        MutableStateFlow(BkgDrawablesRes()) // assigning default value to avoid drawing time
    val mAppBgImageResourceFlow = appBgImageResourceFlow.asStateFlow()
    private val appIconImageResourceFlow: MutableStateFlow<IconDrawables> =
        MutableStateFlow(IconDrawables()) // assigning default value
    val mAppIconImageResourceFlow = appIconImageResourceFlow.asStateFlow()
    private val currentWeatherCodeFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val mCurrentWeatherCodeFlow = currentWeatherCodeFlow.asStateFlow()
    private val mutableStateFlow = MutableStateFlow(true)
    val isLoading = mutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            val jsonUiApiString = application.applicationContext.resources.openRawResource(R.raw.app_ui_config)
                .bufferedReader()
                .use { it.readText() } // For the time being I am reading jsonui response from res/raw folder, it can be a web api call also
            parseJsonUi(jsonUiApiString)

        }
    }


    /**
     * Decided to parse JsonUI api in ViewModel, just to avoid re-parsing the same data on configuration change.
     */
    private fun parseJsonUi(response: String) {
            try {
                val gson = Gson()
                val uiCompose = gson.fromJson(response, UiCompose::class.java)
                //parse the theme
                uiCompose.theme?.light.let {
                    val lightTheme = uiCompose.theme?.light
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

                    if (backgroundDrawables.drawableType?.lowercase(Locale.ROOT) == Constants.IMG_TYPE_URL) {
                        appBgImageResourceFlow.value =
                            BkgDrawablesRes(
                                type = Constants.IMG_TYPE_URL,
                                bgImgUrlResponse = backgroundDrawables
                            )


                    } else if (backgroundDrawables.drawableType?.lowercase(Locale.ROOT) == Constants.IMG_TYPE_DRAWABLE) {
                        appBgImageResourceFlow.value =
                            BkgDrawablesRes(
                                type = Constants.IMG_TYPE_DRAWABLE,
                                bgRaining = AppUtil.getImageId(backgroundDrawables.rainImageSrc),
                                bgCloud = AppUtil.getImageId(backgroundDrawables.cloudImageSrc),
                                bgSnowing = AppUtil.getImageId(backgroundDrawables.snowImageSrc),
                                bgSunny = AppUtil.getImageId(backgroundDrawables.sunnyImageSrc),
                                bgThundar = AppUtil.getImageId(backgroundDrawables.thunderImageSrc)
                            )


                    } else {
                        throw IllegalArgumentException("Drawable type should be either drawable||url")
                    }
                }


                //parsing app icons images
                val iconDrawables = uiCompose.mainScreen?.drawables?.icons
                iconDrawables?.let {
                    if (iconDrawables.drawableType?.lowercase(Locale.ROOT) == Constants.IMG_TYPE_URL) {
                        appIconImageResourceFlow.value =
                            IconDrawables(
                                type = Constants.IMG_TYPE_URL,
                                iconImgUrlResponse = iconDrawables
                            )


                    } else if (iconDrawables.drawableType?.lowercase(Locale.ROOT) == Constants.IMG_TYPE_DRAWABLE) {
                        appIconImageResourceFlow.value =
                            IconDrawables(
                                type = Constants.IMG_TYPE_DRAWABLE,
                                iconRaining = AppUtil.getImageId(iconDrawables.rainImageSrc),
                                iconCloud = AppUtil.getImageId(iconDrawables.cloudImageSrc),
                                iconSnowing = AppUtil.getImageId(iconDrawables.snowImageSrc),
                                iconSunny = AppUtil.getImageId(iconDrawables.sunnyImageSrc),
                                iconThundar = AppUtil.getImageId(iconDrawables.thunderImageSrc)
                            )


                    } else {
                        throw IllegalArgumentException("Drawable type should be either drawable||url")
                    }
                }

                uiComposeFlow.value = uiCompose
                mutableStateFlow.value= false
            } catch (th: Throwable) {
                Log.d("Exception is caught while parsing Json api", th.toString())
                mutableStateFlow.value= false
            }

    }

    fun fetWeatherData(latitude: Double?, longitude: Double?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (latitude != null) {
                if (longitude != null) {
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

                        override fun onFailure(message: String?) {
                            webApiFlowData.value = null
                        }

                    }, latitude, longitude)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}