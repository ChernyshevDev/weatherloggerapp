package com.chernyshev.weatherloggerapp.presentation.map_activity

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.domain.contract.AddressProvider
import com.chernyshev.weatherloggerapp.domain.contract.WeatherProvider
import com.chernyshev.weatherloggerapp.domain.entity.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapsActivityViewModel @Inject constructor(
    private val weatherProvider: WeatherProvider,
    private val addressProvider: AddressProvider,
    private val context: Context
) : ViewModel() {

    var currentSelectionMarker: Marker? = null

    fun saveLocation(location: Location) {
        Log.d("kek", "saved location")
    }

    suspend fun getWeatherDescriptionItems(coordinates: Coordinates): List<Info> {
        var weather: Weather? = null
        withContext((Dispatchers.IO)) {
            weather = weatherProvider.getCurrentWeather(coordinates)
        }
        return weather!!.toViewData().toInfoList()
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
        addressProvider.getAddress(coordinates)

    fun toCoordinates(latlng: LatLng): Coordinates {
        return Coordinates(
            latitude = latlng.latitude,
            longitude = latlng.longitude
        )
    }
}