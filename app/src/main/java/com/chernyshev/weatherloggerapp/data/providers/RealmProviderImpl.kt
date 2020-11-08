package com.chernyshev.weatherloggerapp.data.providers

import com.chernyshev.weatherloggerapp.domain.contract.RealmProvider
import com.chernyshev.weatherloggerapp.domain.entity.Location
import com.chernyshev.weatherloggerapp.domain.entity.realm.StoredLocation
import com.chernyshev.weatherloggerapp.domain.entity.realm.toId
import com.chernyshev.weatherloggerapp.domain.entity.realm.toLocation
import com.chernyshev.weatherloggerapp.domain.entity.realm.toRealmLocation
import io.realm.Realm
import javax.inject.Inject

class RealmProviderImpl @Inject constructor() : RealmProvider {
    override fun saveLocation(location: Location) {
        val realm = Realm.getDefaultInstance()
        try {
            realm.beginTransaction()
            realm.copyToRealm(location.toRealmLocation())
            realm.commitTransaction()
        } finally {
            realm.close()
        }
    }

    override fun removeLocationFromSavings(location: Location) {
        val realm = Realm.getDefaultInstance()
        try {
            val all = realm.where(StoredLocation::class.java).findAll()
            for (saving in all) {
                if (saving.id == location.coordinates.toId()) {
                    realm.executeTransaction {
                        saving.deleteFromRealm()
                    }
                    break
                }
            }
        } finally {
            realm.close()
        }
    }

    override fun getAllSavedLocations(): List<Location> {
        val realm = Realm.getDefaultInstance()
        try {
            val locations = realm.where(StoredLocation::class.java).findAll()
            val listOfLocations = mutableListOf<Location>()
            for (location in locations) {
                listOfLocations.add(location.toLocation())
            }
            return listOfLocations
        } finally {
            realm.close()
        }
    }
}