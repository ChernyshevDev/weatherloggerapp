package com.chernyshev.weatherloggerapp.domain.contract

import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.chernyshev.weatherloggerapp.domain.entity.Location

interface LocationProvider {
    fun getLocation(coordinates: Coordinates): Location
}