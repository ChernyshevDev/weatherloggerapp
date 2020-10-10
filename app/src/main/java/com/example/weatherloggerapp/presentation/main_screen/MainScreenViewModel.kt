package com.example.weatherloggerapp.presentation.main_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weatherloggerapp.LocationDisabledException
import com.example.weatherloggerapp.NetworkDisabledException
import com.example.weatherloggerapp.domain.contract.WeatherProvider
import com.example.weatherloggerapp.domain.entity.Weather
import com.example.weatherloggerapp.presentation.ViewStateHolder
import com.example.weatherloggerapp.presentation.ViewStateHolderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val weatherProvider: WeatherProvider
) : ViewModel(),
    ViewStateHolder<MainScreenViewState> by ViewStateHolderImpl() {

    private lateinit var makeInternetDisabledToast: () -> Unit
    private lateinit var makeLocationServicesDisabledToast: () -> Unit
    private lateinit var makeUnknownIssueToast: () -> Unit

    init {
        updateWeather()
    }

    fun updateWeather() {
        GlobalScope.launch {
            try {
                val weather = weatherProvider.getCurrentWeather()
                updateState {
                    MainScreenViewState(
                        weather = weather
                    )
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    when (exception) {
                        is NetworkDisabledException -> makeInternetDisabledToast()
                        is LocationDisabledException -> makeLocationServicesDisabledToast()
                        else -> {
                            makeUnknownIssueToast()
                            Log.d("kek", exception.stackTraceToString())
                        }
                    }
                }
            }
        }
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

data class MainScreenViewState(
    val weather: Weather
)