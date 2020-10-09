package com.example.weatherloggerapp.data.providers

import com.example.weatherloggerapp.data.retrofit.WeatherResponse
import com.example.weatherloggerapp.data.retrofit.WeatherService
import com.example.weatherloggerapp.data.retrofit.toWeather
import com.example.weatherloggerapp.domain.contract.CoordinatesProvider
import com.example.weatherloggerapp.domain.contract.WeatherProvider
import com.example.weatherloggerapp.domain.entity.Coordinates
import com.example.weatherloggerapp.domain.entity.Weather
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

const val openWeatherMapBaseUrl = "http://api.openweathermap.org"

class WeatherProviderImpl @Inject constructor(
    private val coordinatesProvider: CoordinatesProvider
) : WeatherProvider {

    private lateinit var coordinates: Coordinates

    override suspend fun getCurrentWeather(): Weather {
        coordinates = coordinatesProvider.getCoordinates()
        val weatherRequest = getWeatherRequestCall()
        val responseFromServer = weatherRequest.makeRequest()

        return responseFromServer!!.toWeather()
    }

    private fun getWeatherRequestCall(): Call<WeatherResponse> {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(openWeatherMapBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherService: WeatherService = retrofit.create(WeatherService::class.java)
        return weatherService.getWeather(lat = coordinates.latitude, lon = coordinates.longitude)
    }

    private fun Call<WeatherResponse>.makeRequest() =
        this.execute()
            .body()

}