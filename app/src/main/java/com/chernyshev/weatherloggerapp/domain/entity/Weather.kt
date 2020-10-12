package com.chernyshev.weatherloggerapp.domain.entity

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

data class Weather(
    val temperature: Int,
    val city: String,
    val timeStamp: Long,
    val longitude: Double,
    val latitude: Double,
    val description: String,
    val iconId: Int,
    val pressure: Int,
    val windSpeed: Double,
)

data class DateTime(
    val date: String,
    val time: String
)

data class WeatherViewData(
    val temperature: String,
    val city: String,
    val date: String,
    val time: String,
    val description: String,
    val pressure: String,
    val windSpeed: String
)

fun Weather.toViewData() : WeatherViewData{
    return WeatherViewData(
        temperature = this.temperature.toString() + "°C",
        city = this.city,
        date = this.timeStamp.toDate(),
        time = this.timeStamp.toTime(),
        description = this.description,
        pressure = this.pressure.toString() + " hPa",
        windSpeed = this.windSpeed.toString() + " m/sec"
    )
}

fun Long.toTime() : String{
    val timeStamp = Timestamp(this)
    val timeFormat = SimpleDateFormat("HH:mm")

    return timeFormat.format(timeStamp)
}

fun Long.toDate() : String{
    val timeStamp = Timestamp(this)
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")

    return dateFormat.format(timeStamp)
}
