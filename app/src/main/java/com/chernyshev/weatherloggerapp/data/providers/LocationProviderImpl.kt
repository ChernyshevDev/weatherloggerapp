package com.chernyshev.weatherloggerapp.data.providers

import android.content.Context
import android.location.Geocoder
import com.chernyshev.weatherloggerapp.domain.contract.LocationProvider
import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.chernyshev.weatherloggerapp.domain.entity.Location
import java.util.*
import javax.inject.Inject

class LocationProviderImpl @Inject constructor(
    private val context: Context,
) : LocationProvider {
    override fun getLocation(coordinates: Coordinates): Location {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addressList = geocoder.getFromLocation(coordinates.latitude, coordinates.longitude, 1)
        var country: String? = null
        var city: String? = null
        addressList[0]?.let {
            city = it.adminArea ?: it.subAdminArea ?: it.locality ?: it.featureName
            country = it.countryName
        }
        return Location(
            city = city ?: "Unknown city",
            country = country ?: "Unknown country",
            coordinates = coordinates
        )
    }
}