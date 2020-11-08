package com.chernyshev.weatherloggerapp.presentation.main_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chernyshev.weatherloggerapp.LocationDisabledException
import com.chernyshev.weatherloggerapp.NetworkDisabledException
import com.chernyshev.weatherloggerapp.domain.contract.DatabaseProvider
import com.chernyshev.weatherloggerapp.domain.contract.RealmProvider
import com.chernyshev.weatherloggerapp.domain.contract.WeatherProvider
import com.chernyshev.weatherloggerapp.domain.entity.Location
import com.chernyshev.weatherloggerapp.domain.entity.Weather
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val weatherProvider: WeatherProvider,
    private val database: DatabaseProvider,
    private val realmProvider: RealmProvider
) : ViewModel() {

    var otherCitiesWeathers: MutableLiveData<List<Pair<Location, Weather>>> = MutableLiveData()
    var weatherInCurrentLocation: MutableLiveData<Weather> = MutableLiveData()

    private lateinit var makeInternetDisabledToast: () -> Unit
    private lateinit var makeLocationServicesDisabledToast: () -> Unit
    private lateinit var makeUnknownIssueToast: () -> Unit

    init {
        GlobalScope.launch {
            fetchViewData()
        }
    }

    private suspend fun fetchViewData() {
        try {
            updateWeatherInCurrentLocation()
            updateOtherCitiesWeathers()
        } catch (exception: Exception) {
            withContext(Dispatchers.Main) {
                when (exception) {
                    is NetworkDisabledException -> makeInternetDisabledToast()
                    is LocationDisabledException -> makeLocationServicesDisabledToast()
                    else -> makeUnknownIssueToast()
                }
            }
        }
    }

    suspend fun updateWeatherInCurrentLocation() {
        weatherInCurrentLocation.postValue(weatherProvider.getLocalWeather())
    }

    fun saveCurrentWeather() {
        database.saveCurrentWeather(weatherInCurrentLocation.value!!)
    }

    fun deleteSavedLocation(location: Location) {
        GlobalScope.launch {
            realmProvider.removeLocationFromSavings(location)
            updateOtherCitiesWeathers()
        }
    }

    suspend fun updateOtherCitiesWeathers() {
        val listOfWeathers = getSavedLocationsWeathers()
        otherCitiesWeathers.postValue(
            listOfWeathers
        )
    }

    private suspend fun getSavedLocationsWeathers(): List<Pair<Location, Weather>> {
        val listOfWeathers = mutableListOf<Pair<Location, Weather>>()

        val listOfLocations = realmProvider.getAllSavedLocations()
        for (location in listOfLocations) {
            val weather = weatherProvider.getWeatherIn(location.coordinates)
            listOfWeathers.add(Pair(location, weather))
        }

        return listOfWeathers
    }

    fun setLocationDisabledToast(doing: () -> Unit) {
        makeLocationServicesDisabledToast = doing
    }

    fun setInternetDisabledToast(doing: () -> Unit) {
        makeInternetDisabledToast = doing
    }

    fun setUnknownIssueToast(doing: () -> Unit) {
        makeUnknownIssueToast = doing
    }
}