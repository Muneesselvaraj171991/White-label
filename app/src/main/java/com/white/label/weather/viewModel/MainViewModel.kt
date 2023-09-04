package com.white.label.weather.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dog.app.network.RemoteCall
import com.google.gson.Gson
import com.white.label.weather.model.Weather
import com.white.label.weather.ui.model.UiCompose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainViewModel: ViewModel() {
    private val mRemoteCall: RemoteCall = RemoteCall.remoteCallInstance
     var uiComposeLivedata: MutableLiveData<UiCompose> = MutableLiveData()
    var webApiLiveData: MutableLiveData<Weather> = MutableLiveData()

    fun parseJsonUi(response: String){
        viewModelScope.launch {
            val gson = Gson()
            uiComposeLivedata.value = gson.fromJson(response, UiCompose::class.java)
        }


    }

    fun  fetWeatherData(latitude: Double, longitude : Double ) {
        viewModelScope.async(Dispatchers.IO) {

            mRemoteCall.weatherApi(object : RemoteCall.Result {
                override fun onResponse(weather: Weather) {
                    webApiLiveData.value = weather
                }

                override fun onFailure() {

                }

            }, latitude, longitude)
        }
    }

}