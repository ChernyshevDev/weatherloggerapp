package com.chernyshev.weatherloggerapp.presentation.map_activity

import android.content.Context
import androidx.lifecycle.ViewModel
import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.domain.contract.LocationProvider
import com.chernyshev.weatherloggerapp.domain.contract.RealmProvider
import com.chernyshev.weatherloggerapp.domain.contract.WeatherProvider
import com.chernyshev.weatherloggerapp.domain.entity.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapsActivityViewModel @Inject constructor(
    private val weatherProvider: WeatherProvider,
    private val locationProvider: LocationProvider,
    private val realmProvider: RealmProvider,
    private val context: Context
) : ViewModel() {

    var currentSelectionMarker: Marker? = null
    var savedLocations: List<Location> = listOf()
    var savedLocationsMarkers: MutableList<Pair<Marker, Location>> = mutableListOf()

    init {
        fetchSavedLocations()
    }

    fun saveLocation(location: Location) {
        realmProvider.saveLocation(location)
        fetchSavedLocations()
    }

    fun removeLocationSaving(location: Location) {
        GlobalScope.launch {
            realmProvider.removeLocationFromSavings(location)
        }
    }

    suspend fun getWeatherDescriptionItems(coordinates: Coordinates): List<Info> {
        var weather: Weather?
        withContext((Dispatchers.IO)) {
            weather = weatherProvider.getWeatherIn(coordinates)
        }
        return weather!!.toViewData(context).toInfoList()
    }

    private fun fetchSavedLocations() {
        savedLocations = realmProvider.getAllSavedLocations()
    }

    private fun WeatherViewData.toInfoList(): List<Info> =
        listOf(
            Info(
                context.getString(R.string.temperature),
                this.temperature
            ),
            Info(
                context.getString(R.string.description),
                this.description
            ),
            Info(
                context.getString(R.string.wind_speed),
                this.windSpeed
            ),
            Info(
                context.getString(R.string.pressure),
                this.pressure
            )
        )

    fun toLocation(coordinates: Coordinates): Location =
        locationProvider.getLocation(coordinates)

    fun toCoordinates(latlng: LatLng): Coordinates {
        return Coordinates(
            latitude = latlng.latitude,
            longitude = latlng.longitude
        )
    }
}