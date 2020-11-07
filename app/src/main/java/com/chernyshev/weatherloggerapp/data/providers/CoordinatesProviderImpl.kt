package com.chernyshev.weatherloggerapp.data.providers

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import com.chernyshev.weatherloggerapp.LocationDisabledException
import com.chernyshev.weatherloggerapp.NetworkDisabledException
import com.chernyshev.weatherloggerapp.domain.contract.CoordinatesProvider
import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CoordinatesProviderImpl @Inject constructor(
    private val context: Context
) : CoordinatesProvider {

    private lateinit var locationClient: FusedLocationProviderClient
    private var coordinates: Coordinates? = null

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentCoordinates(): Coordinates {
        locationClient = LocationServices.getFusedLocationProviderClient(context)

        if (!isNetworkEnabled()) {
            throw NetworkDisabledException()
        } else if (!isLocationEnabled()) {
            throw LocationDisabledException()
        } else {
            val location = locationClient.lastLocation.await()
            location?.let {
                coordinates = Coordinates(
                    longitude = location.longitude,
                    latitude = location.latitude
                )
            }
            return coordinates!!
        }
    }

    private fun isNetworkEnabled(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}