package com.example.weatherloggerapp.domain.contract

import com.example.weatherloggerapp.domain.entity.Coordinates

interface CoordinatesProvider {
    suspend fun getCoordinates() : Coordinates
}