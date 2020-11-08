package com.chernyshev.weatherloggerapp.data.retrofit

import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.chernyshev.weatherloggerapp.domain.entity.Weather
import kotlin.math.roundToInt

data class WeatherResponse(
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
        city = this.name,
        timeStamp = getCurrentTimestamp(),
        temperature = this.main.temp.roundToInt(),
        description = this.weather[0].description,
        iconId = getIconId(this.weather[0].description),
        pressure = this.main.pressure,
        windSpeed = this.wind.speed,
        coordinates = Coordinates(
            latitude = this.coord.lat,
            longitude = this.coord.lon
        )
    )
}

private fun getIconId(weatherDescription: String): Int {
    return when (weatherDescription) {
        "clear sky" -> R.drawable.ic_sun
        "few clouds" -> R.drawable.ic_clouds
        "scattered clouds" -> R.drawable.ic_clouds
        "broken clouds" -> R.drawable.ic_clouds
        "shower rain" -> R.drawable.ic_rain
        "rain" -> R.drawable.ic_rain
        "thunderstorm" -> R.drawable.ic_thunderstorm
        "snow" -> R.drawable.ic_snow
        "mist" -> R.drawable.ic_fog
        else -> R.drawable.ic_clouds
    }
}

private fun getCurrentTimestamp(): Long {
    return System.currentTimeMillis()
}