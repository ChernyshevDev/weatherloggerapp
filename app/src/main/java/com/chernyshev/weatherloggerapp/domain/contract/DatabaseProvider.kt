package com.chernyshev.weatherloggerapp.domain.contract

import com.chernyshev.weatherloggerapp.domain.entity.Weather

interface DatabaseProvider {
    suspend fun saveCurrentWeather(weather: Weather)
}