package com.example.weatherloggerapp.data.providers

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.util.Log
import com.example.weatherloggerapp.LocationDisabledException
import com.example.weatherloggerapp.NetworkDisabledException
import com.example.weatherloggerapp.domain.contract.CoordinatesProvider
import com.example.weatherloggerapp.domain.entity.Coordinates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class CoordinatesProviderImpl @Inject constructor(
    private val context: Context
) : CoordinatesProvider {

    private lateinit var locationClient: FusedLocationProviderClient
    private var coordinates: Coordinates? = null


    override suspend fun getCoordinates(): Coordinates {

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
        var locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}