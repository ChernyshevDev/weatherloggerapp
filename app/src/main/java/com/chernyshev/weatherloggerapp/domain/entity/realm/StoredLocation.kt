package com.chernyshev.weatherloggerapp.domain.entity.realm

import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.chernyshev.weatherloggerapp.domain.entity.Location
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class StoredLocation(
    @PrimaryKey var id: String = "",
    var country: String = "",
    var city: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : RealmObject()

fun Location.toRealmLocation(): StoredLocation =
    StoredLocation(
        id = coordinates.toId(),
        country = country,
        city = city,
        latitude = coordinates.latitude,
        longitude = coordinates.longitude
    )

fun StoredLocation.toLocation(): Location =
    Location(
        country = country,
        city = city,
        coordinates =
        Coordinates(
            latitude = latitude,
            longitude = longitude
        )
    )

fun Coordinates.toId(): String =
    this.latitude.toString() + ":" + this.longitude.toString()