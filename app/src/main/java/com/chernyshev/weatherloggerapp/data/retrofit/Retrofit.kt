package com.chernyshev.weatherloggerapp.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val openWeatherMapBaseUrl = "http://api.openweathermap.org"
const val accessKey = "e969637b42055b4ad51e22ecc8b7310c"
const val UNIT_METRIC = "metric"

interface WeatherService {
    @GET("/data/2.5/weather?")
    fun getWeather(
        @Query("city") city: String? = null,
        @Query("lat") lat: Double? = null,
        @Query("lon") lon: Double? = null,
        @Query("APPID") key: String = accessKey,
        @Query("units") units: String = UNIT_METRIC,
        @Query("lang") lang: String? = null
    ): Call<WeatherResponse>
}