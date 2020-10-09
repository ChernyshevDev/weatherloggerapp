package com.example.weatherloggerapp.presentation.main_screen

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.weatherloggerapp.LocationDisabledException
import com.example.weatherloggerapp.NetworkDisabledException
import com.example.weatherloggerapp.domain.contract.WeatherProvider
import com.example.weatherloggerapp.domain.entity.Weather
import com.example.weatherloggerapp.presentation.ViewStateHolder
import com.example.weatherloggerapp.presentation.ViewStateHolderImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val weatherProvider: WeatherProvider
) : ViewModel(),
    ViewStateHolder<MainScreenViewState> by ViewStateHolderImpl() {

    init {
        try{
            fetchViewState()
        } catch(exception: Exception){
            when(exception){
                is NetworkDisabledException -> Log.d("kek", "Network disabled Exception thrown")
                is LocationDisabledException -> Log.d("kek", "Location disabled Exception thrown")
            }
        }

    }

    private fun fetchViewState() {

        var weather : Weather? = null
            GlobalScope.launch {
                try{
                    weather = weatherProvider.getCurrentWeather()
                    updateState {
                        MainScreenViewState(
                            weather = weather!!
                        )
                    }
                } catch(exception: Exception){
                    when(exception){
                        is NetworkDisabledException -> Log.d("kek", "Network disabled Exception thrown")
                        is LocationDisabledException -> Log.d("kek", "Location disabled Exception thrown")
                        else -> Log.d("kek", "UNKNOWN Exception thrown")
                    }
                }
        }
    }
}

data class MainScreenViewState(
    val weather: Weather
)