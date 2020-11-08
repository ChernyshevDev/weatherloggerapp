package com.chernyshev.weatherloggerapp.domain.entity

import android.content.Context
import com.chernyshev.weatherloggerapp.R
import java.sql.Timestamp
import java.text.SimpleDateFormat

data class Weather(
    val temperature: Int,
    val city: String,
    val timeStamp: Long,
    var description: String,
    val iconId: Int = 0,
    val pressure: Int,
    val windSpeed: Double,
    val coordinates: Coordinates? = null
)

data class WeatherViewData(
    val timeStamp: Long,
    val temperature: String,
    val city: String,
    val date: String,
    val time: String,
    val description: String,
    val pressure: String,
    val windSpeed: String
)

fun Weather.toViewData(context: Context): WeatherViewData {
    return WeatherViewData(
        timeStamp = this.timeStamp,
        temperature = this.temperature.toString() + "°C",
        city = this.city,
        date = this.timeStamp.toDate(),
        time = this.timeStamp.toTime(),
        description = this.description,
        pressure = String.format(
            context.getString(R.string.hpa),
            this.pressure
        ),
        windSpeed = String.format(
            context.getString(R.string.m_per_sec),
            this.windSpeed.toInt()
        )
    )
}

fun Long.toTime(): String {
    val timeStamp = Timestamp(this)
    val timeFormat = SimpleDateFormat("HH:mm")

    return timeFormat.format(timeStamp)
}

fun Long.toDate(): String {
    val timeStamp = Timestamp(this)
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")

    return dateFormat.format(timeStamp)
}
