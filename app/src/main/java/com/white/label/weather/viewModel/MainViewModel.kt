package com.white.label.weather.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dog.app.network.RemoteCall
import com.google.gson.Gson
import com.white.label.weather.model.Weather
import com.white.label.weather.ui.model.UiCompose
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
import java.util.Locale


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val mRemoteCall: RemoteCall = RemoteCall.remoteCallInstance
    var uiComposeLivedata: MutableLiveData<UiCompose> = MutableLiveData()
    var webApiLiveData: MutableLiveData<Weather> = MutableLiveData()
    var appBgImageResourceLiveData: MutableLiveData<BkgDrawablesRes> =
        MutableLiveData(BkgDrawablesRes()) // assigning default value to avoid drawing time
    var appIconImageResourceLiveData: MutableLiveData<IconDrawables> =
        MutableLiveData(IconDrawables()) // assigning default value
    var currentWeatherCodeLiveData: MutableLiveData<Int> = MutableLiveData(0)


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
                        appBgImageResourceLiveData.postValue(
                            BkgDrawablesRes(
                                type = Constants.IMG_TYPE_URL,
                                bgImgUrlResponse = backgroundDrawables
                            )
                        )

                    } else if (backgroundDrawables.drawableType.lowercase(Locale.ROOT) == Constants.IMG_TYPE_DRAWABLE) {
                        appBgImageResourceLiveData.postValue(
                            BkgDrawablesRes(
                                type = Constants.IMG_TYPE_DRAWABLE,
                                bgRaining = AppUtil.getImageId(backgroundDrawables.rainImageSrc!!),
                                bgCloud = AppUtil.getImageId(backgroundDrawables.cloudImageSrc!!),
                                bgSnowing = AppUtil.getImageId(backgroundDrawables.snowImageSrc!!),
                                bgSunny = AppUtil.getImageId(backgroundDrawables.sunnyImageSrc!!),
                                bgThundar = AppUtil.getImageId(backgroundDrawables.thunderImageSrc!!)
                            )
                        )

                    } else {
                        throw IllegalArgumentException("Drawable type should be either drawable||url")
                    }
                }


                //parsing app icons images
                val iconDrawables = uiCompose.mainScreen?.drawables?.icons
                iconDrawables?.let {
                    if (iconDrawables.drawableType!!.lowercase(Locale.ROOT) == Constants.IMG_TYPE_URL) {
                        appIconImageResourceLiveData.postValue(
                            IconDrawables(
                                type = Constants.IMG_TYPE_URL,
                                iconImgUrlResponse = iconDrawables
                            )
                        )

                    } else if (iconDrawables.drawableType.lowercase(Locale.ROOT) == Constants.IMG_TYPE_DRAWABLE) {
                        appIconImageResourceLiveData.postValue(
                            IconDrawables(
                                type = Constants.IMG_TYPE_DRAWABLE,
                                iconRaining = AppUtil.getImageId(iconDrawables.rainImageSrc!!),
                                iconCloud = AppUtil.getImageId(iconDrawables.cloudImageSrc!!),
                                iconSnowing = AppUtil.getImageId(iconDrawables.snowImageSrc!!),
                                iconSunny = AppUtil.getImageId(iconDrawables.sunnyImageSrc!!),
                                iconThundar = AppUtil.getImageId(iconDrawables.thunderImageSrc!!)
                            )
                        )

                    } else {
                        throw IllegalArgumentException("Drawable type should be either drawable||url")
                    }
                }

                uiComposeLivedata.postValue(uiCompose)

            } catch (th: Throwable) {
                Log.d("Exception is caught while parsing Json api", th.toString())
            }

        }

    }

    fun fetWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.async(Dispatchers.IO) {

            mRemoteCall.weatherApi(object : RemoteCall.Result {
                override fun onResponse(weather: Weather) {
                    currentWeatherCodeLiveData.postValue(weather.current_weather.weathercode)
                    webApiLiveData.postValue(weather)
                }

                override fun onFailure() {

                }

            }, latitude, longitude)
        }
    }

}