package com.chernyshev.weatherloggerapp.domain.contract

import com.chernyshev.weatherloggerapp.domain.entity.Coordinates

interface CoordinatesProvider {
    suspend fun getCoordinates(): Coordinates
}