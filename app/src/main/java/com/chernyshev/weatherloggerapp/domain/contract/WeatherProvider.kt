package com.chernyshev.weatherloggerapp.domain.contract

import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.chernyshev.weatherloggerapp.domain.entity.Weather

interface WeatherProvider {
    suspend fun getCurrentWeather(coordinates: Coordinates? = null): Weather
}