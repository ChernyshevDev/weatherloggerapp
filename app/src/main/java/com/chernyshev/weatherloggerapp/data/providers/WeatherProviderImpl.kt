package com.chernyshev.weatherloggerapp.data.providers

import com.chernyshev.weatherloggerapp.data.retrofit.WeatherResponse
import com.chernyshev.weatherloggerapp.data.retrofit.WeatherService
import com.chernyshev.weatherloggerapp.data.retrofit.openWeatherMapBaseUrl
import com.chernyshev.weatherloggerapp.data.retrofit.toWeather
import com.chernyshev.weatherloggerapp.domain.contract.CoordinatesProvider
import com.chernyshev.weatherloggerapp.domain.contract.WeatherProvider
import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.chernyshev.weatherloggerapp.domain.entity.Weather
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class WeatherProviderImpl @Inject constructor(
    private val coordinatesProvider: CoordinatesProvider
) : WeatherProvider {

    override suspend fun getLocalWeather(): Weather {
        val coordinates = coordinatesProvider.getCurrentCoordinates()
        val weatherRequest = getWeatherRequestCall(coordinates = coordinates)
        val responseFromServer = weatherRequest.makeRequest()

        return responseFromServer!!.toWeather()
    }

    override suspend fun getWeatherIn(city: String): Weather {
        val weatherRequest = getWeatherRequestCall(city = city)
        val responseFromServer = weatherRequest.makeRequest()

        return responseFromServer!!.toWeather()
    }

    override suspend fun getWeatherIn(coordinates: Coordinates): Weather {
        val weatherRequest = getWeatherRequestCall(coordinates)
        val responseFromServer = weatherRequest.makeRequest()

        return responseFromServer!!.toWeather()
    }

    private fun getWeatherRequestCall(coordinates: Coordinates? = null, city: String? = null): Call<WeatherResponse> {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(openWeatherMapBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherService: WeatherService = retrofit.create(WeatherService::class.java)
        return if(coordinates != null){
            weatherService.getWeather(lat = coordinates.latitude, lon = coordinates.longitude)
        } else weatherService.getWeather(city = city!!)
    }

    private fun Call<WeatherResponse>.makeRequest() =
        this.execute()
            .body()

}