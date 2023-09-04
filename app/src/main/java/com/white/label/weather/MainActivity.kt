package com.example.white_label

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import com.example.white_label.ui.theme.WhitelabelTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.white.label.weather.ui.model.UiCompose
import com.white.label.weather.util.AppUtil
import com.white.label.weather.view.currentWeatherScreen
import com.white.label.weather.view.dayPredictionScreen
import com.white.label.weather.view.daysPredictionList
import com.white.label.weather.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val viewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext!!)

        val jsonUiApi = resources.openRawResource(R.raw.app_ui_config)
            .bufferedReader().use { it.readText() }

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
            val uiCompose = viewModel.uiComposeLivedata.observeAsState()
            LaunchedEffect(key1 = Unit) {
                viewModel.parseJsonUi(jsonUiApi)
            }

            uiCompose.value?.let {
                WhitelabelTheme(it.theme) {
                    val imageSrc = AppUtil.getImageId(this@MainActivity,"cloudy")
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .paint(
                                painterResource(id = imageSrc),
                                contentScale = ContentScale.FillBounds
                            )
                    ) {
                        var weatherLiveData = viewModel.webApiLiveData.observeAsState()
                       for ( banner in it.mainScreen.banners!!) {
                           val bannerUnit = banner.getBannerUnit()
                           Row(modifier = Modifier.weight(bannerUnit.weight, true)) {
                               when (bannerUnit.type) {
                                   "current_weather" -> currentWeatherScreen()
                                   "day_prediction" -> dayPredictionScreen()
                                   "prediction_list" -> daysPredictionList()
                               }
                           }
                       }

                    }
                }
            }
            }
        }



    fun getLastKnownLocation() {
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
                }

            }

    }

}




