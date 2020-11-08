package com.chernyshev.weatherloggerapp.domain.contract

import com.chernyshev.weatherloggerapp.domain.entity.Location

interface RealmProvider {
    fun saveLocation(location: Location)
    fun removeLocationFromSavings(location: Location)
    fun getAllSavedLocations(): List<Location>
}