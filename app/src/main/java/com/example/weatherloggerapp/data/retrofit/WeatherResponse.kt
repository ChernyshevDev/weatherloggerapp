package com.example.weatherloggerapp.data.retrofit

import com.example.weatherloggerapp.domain.entity.Weather
import java.text.SimpleDateFormat
import java.util.*

data class WeatherResponse( // TODO try sealed class
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
) {
    data class Clouds(
        val all: Int
    )

    data class Coord(
        val lat: Double,
        val lon: Double
    )

    data class Main(
        val humidity: Int,
        val pressure: Int,
        val temp: Double,
        val temp_max: Double,
        val temp_min: Double
    )

    data class Sys(
        val country: String,
        val id: Int,
        val message: Double,
        val sunrise: Int,
        val sunset: Int,
        val type: Int
    )

    data class Weather(
        val description: String,
        val icon: String,
        val id: Int,
        val main: String
    )

    data class Wind(
        val deg: Int,
        val speed: Double
    )
}

fun WeatherResponse.toWeather(): Weather {
    return Weather(
        city = this.sys.country,
        dateTime = getCurrentTime(),
        temperature = this.main.temp.toFloat(),
        longitude = this.coord.lon,
        latitude = this.coord.lat
    )
}

private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
    return sdf.format(Date()).toString()
}