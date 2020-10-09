package com.example.weatherloggerapp.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val accessKey = "e969637b42055b4ad51e22ecc8b7310c"
const val unit = "metric"

interface WeatherService {
    @GET("/data/2.5/weather?")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("APPID") key: String = accessKey,
        @Query("units") units: String = unit
    ): Call<WeatherResponse>
}