package com.chernyshev.weatherloggerapp.domain.contract

import com.chernyshev.weatherloggerapp.domain.entity.Weather

interface DatabaseProvider {
    fun saveCurrentWeather(weather: Weather)

    suspend fun getLastSaving() : Weather

    suspend fun getAllSavings() : List<Weather>
}