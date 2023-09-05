package com.dog.app.network


import com.google.gson.Gson
import com.white.label.weather.model.Weather
import java.net.HttpURLConnection
import java.net.URL

class RemoteCall private constructor() {
    private val mGson: Gson = Gson()
    fun weatherApi(result: Result, latitude: Double, longitude: Double) {
        try {
            val urlConnection =
                URL("$BASE_URL?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,weathercode&daily=temperature_2m_max,temperature_2m_min,weathercode&current_weather=true&timezone=GMT").openConnection() as HttpURLConnection
            val data = urlConnection.inputStream.bufferedReader().readText()
            result.onResponse(mGson.fromJson(data, Weather::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
            result.onFailure()
        }
    }


    interface Result {
        fun onResponse(weather: Weather)
        fun onFailure()
    }

    companion object {
        private const val BASE_URL = "https://api.open-meteo.com/v1/forecast"
        private var sRemoteCall: RemoteCall? = null

        @get:Synchronized
        val remoteCallInstance: RemoteCall
            get() {
                if (sRemoteCall == null) {
                    sRemoteCall = RemoteCall()
                }
                return sRemoteCall!!
            }
    }
}