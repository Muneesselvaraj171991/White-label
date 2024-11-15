package com.white.label.weather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.white_label.R
import com.white.label.weather.view.ui.theme.MyApplicationTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.white.label.weather.respository.CheckInternetConnection
import com.white.label.weather.view.ui.theme.darkPrimaryColor
import com.white.label.weather.view.ui.theme.darkSecondaryColor
import com.white.label.weather.view.ui.theme.darkTertiaryColor
import com.white.label.weather.view.ui.theme.lightPrimaryColor
import com.white.label.weather.view.ui.theme.lightSecondaryColor
import com.white.label.weather.view.ui.theme.lightTertiaryColor
import com.white.label.weather.view.compose.MainScreen
import com.white.label.weather.viewModel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val viewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mCurrentLocation: Location? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value }
        val networkConnection = CheckInternetConnection()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
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
            val snackbarHostState = remember { SnackbarHostState() }
            val netWorkConnected = networkConnection.observeAsState().value
            if (netWorkConnected == false) {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.resources.getString(R.string.no_internet_connection),
                        duration = SnackbarDuration.Short
                    )
                }
            } else {
                if (mCurrentLocation != null) {
                    viewModel.fetWeatherData(
                        mCurrentLocation?.latitude,
                        mCurrentLocation?.longitude
                    )
                } else {
                    //Purely for testing purpose in emulator when location is not found
                    viewModel.fetWeatherData(
                        59.2069783, 17.9066208
                    )

                }
            }

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                val uiCompose by viewModel.mUiComposeFlow.collectAsStateWithLifecycle()
                uiCompose?.let {
                    MyApplicationTheme(
                        lightThemeColor = lightColorScheme(
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
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            val weatherApi by viewModel.mWebApiFlowData.collectAsStateWithLifecycle()
                            MainScreen(weatherApi, viewModel, uiCompose)
                            if (weatherApi == null) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .alpha(0f)
                                )
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
                mCurrentLocation = location
            }
            .addOnFailureListener {
                mCurrentLocation = null
            }
            .addOnCanceledListener {
                mCurrentLocation = null
            }
    }
}







