package com.chernyshev.weatherloggerapp.domain.contract

import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.chernyshev.weatherloggerapp.domain.entity.Weather

interface WeatherProvider {
    suspend fun getLocalWeather(): Weather
    suspend fun getWeatherIn(city: String): Weather
    suspend fun getWeatherIn(coordinates: Coordinates): Weather
}