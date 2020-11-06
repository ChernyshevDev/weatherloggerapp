package com.chernyshev.weatherloggerapp.presentation.map_activity

import android.util.Log
import androidx.lifecycle.ViewModel
import com.chernyshev.weatherloggerapp.domain.contract.WeatherProvider
import com.chernyshev.weatherloggerapp.domain.entity.Location
import com.chernyshev.weatherloggerapp.presentation.ViewStateHolder
import com.chernyshev.weatherloggerapp.presentation.ViewStateHolderImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapsActivityViewModel @Inject constructor(
    private val weatherProvider: WeatherProvider
) : ViewModel(),
    ViewStateHolder<MapsActivityViewState> by ViewStateHolderImpl() {


    fun saveLocation(location : Location) {
        Log.d("kek","saved location")
    }

    fun getWeatherDescriptionItems(location: Location){
        GlobalScope.launch {
            val weather = weatherProvider.getCurrentWeather()
        }

    }

}

data class MapsActivityViewState()

