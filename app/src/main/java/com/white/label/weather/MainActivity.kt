package com.example.white_label

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.white.label.weather.ui.theme.darkH1TextSize
import com.white.label.weather.ui.theme.darkH2TextSize
import com.white.label.weather.ui.theme.darkNormalTextSize
import com.white.label.weather.ui.theme.darkPrimaryColor
import com.white.label.weather.ui.theme.darkSecondaryColor
import com.white.label.weather.ui.theme.darkTertiaryColor
import com.white.label.weather.ui.theme.darkTextColor
import com.white.label.weather.ui.theme.h1TextSize
import com.white.label.weather.ui.theme.h2TextSize
import com.white.label.weather.ui.theme.lightH1TextSize
import com.white.label.weather.ui.theme.lightH2TextSize
import com.white.label.weather.ui.theme.lightNormalTextSize
import com.white.label.weather.ui.theme.lightPrimaryColor
import com.white.label.weather.ui.theme.lightSecondaryColor
import com.white.label.weather.ui.theme.lightTertiaryColor
import com.white.label.weather.ui.theme.lightTextColor
import com.white.label.weather.ui.theme.normalTextSize
import com.white.label.weather.ui.theme.textColor
import com.white.label.weather.util.AppUtil
import com.white.label.weather.util.Constants.Companion.BANNER_TYPE_CURRENT_WEATHER
import com.white.label.weather.util.Constants.Companion.BANNER_TYPE_DAY_PREDICTION
import com.white.label.weather.util.Constants.Companion.BANNER_TYPE_PREDICTION_LIST
import com.white.label.weather.util.Constants.Companion.IMG_TYPE_DRAWABLE
import com.white.label.weather.view.CurrentWeatherScreen
import com.white.label.weather.view.DayPredictionScreen
import com.white.label.weather.view.DaysPredictionList
import com.white.label.weather.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val viewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext!!)

        val jsonUiApiString = resources.openRawResource(R.raw.app_ui_config)
            .bufferedReader()
            .use { it.readText() } // For the time being I am reading jsonui response from res/raw folder, it can be a web api call also
        viewModel.parseJsonUi(jsonUiApiString)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            getLastKnownLocation()
        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )

        setContent {
            val uiCompose by viewModel.uiComposeLivedata.observeAsState()
            uiCompose?.let {

                MyApplicationTheme(
                    lightThemeColor = darkColorScheme(
                        primary = lightPrimaryColor,
                        secondary = lightSecondaryColor,
                        tertiary = lightTertiaryColor
                    ),
                    darkColorScheme(
                        primary = darkPrimaryColor,
                        secondary = darkSecondaryColor,
                        tertiary = darkTertiaryColor
                    )
                ) {
                    val bgImage by viewModel.appBgImageResourceLiveData.observeAsState()
                    val currentWeatherCode by viewModel.currentWeatherCodeLiveData.observeAsState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .paint(
                                if (bgImage!!.type == IMG_TYPE_DRAWABLE) painterResource(
                                    id = AppUtil.getBgImageUrl(
                                        currentWeatherCode!!,
                                        bgImage!!
                                    )
                                ) else rememberAsyncImagePainter(
                                    AppUtil.getBgImageUrl(
                                        currentWeatherCode!!,
                                        bgImage!!.bgImgUrlResponse!!
                                    )
                                ),
                                contentScale = ContentScale.FillBounds
                            )
                    ) {

                        if(isSystemInDarkTheme()) {
                            h1TextSize = darkH1TextSize.sp
                            h2TextSize = darkH2TextSize.sp
                            normalTextSize = darkNormalTextSize.sp
                            textColor = darkTextColor

                        } else {
                            h1TextSize = lightH1TextSize.sp
                            h2TextSize = lightH2TextSize.sp
                            normalTextSize = lightNormalTextSize.sp
                            textColor = lightTextColor

                        }
                        for (banner in it.mainScreen?.banners!!) {
                            val bannerUnit = banner.getBannerUnit()
                            Row(modifier = Modifier.weight(bannerUnit.weight, true)) {
                                when (bannerUnit.type) {
                                    BANNER_TYPE_CURRENT_WEATHER -> CurrentWeatherScreen(
                                        viewModel,
                                        banner.backgroundColor
                                    )

                                    BANNER_TYPE_DAY_PREDICTION -> DayPredictionScreen(
                                        viewModel,
                                        banner.backgroundColor,
                                        banner.title!!
                                    )

                                    BANNER_TYPE_PREDICTION_LIST -> DaysPredictionList(
                                        viewModel,
                                        banner.backgroundColor,
                                        banner.title!!
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }


    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    viewModel.fetWeatherData(location.latitude, location.longitude)
                } else {
                    //Purely for testing purpose in emulator when location is not found
                    viewModel.fetWeatherData(57.7344, 11.8797)

                }

            }

    }

}




