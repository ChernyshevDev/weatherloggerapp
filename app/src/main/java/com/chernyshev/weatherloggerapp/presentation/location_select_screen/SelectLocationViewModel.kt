package com.chernyshev.weatherloggerapp.presentation.location_select_screen

import androidx.lifecycle.ViewModel
import com.chernyshev.weatherloggerapp.domain.contract.LocationProvider
import com.chernyshev.weatherloggerapp.domain.contract.RealmProvider
import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectLocationViewModel @Inject constructor(
    private val locationProvider: LocationProvider,
    private val realmProvider: RealmProvider
) : ViewModel() {
    fun savePlace(place: Place) {
        GlobalScope.launch {
            try {
                val coordinates = Coordinates(
                    place.latLng!!.longitude,
                    place.latLng!!.latitude
                )
                val location = locationProvider.getLocation(coordinates)
                realmProvider.saveLocation(location)
            } catch (e: Exception) {
            }
        }
    }
}