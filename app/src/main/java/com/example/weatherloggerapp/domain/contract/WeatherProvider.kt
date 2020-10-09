package com.example.weatherloggerapp.domain.contract

import com.example.weatherloggerapp.domain.entity.Weather

interface WeatherProvider {
    suspend fun getCurrentWeather(): Weather
}