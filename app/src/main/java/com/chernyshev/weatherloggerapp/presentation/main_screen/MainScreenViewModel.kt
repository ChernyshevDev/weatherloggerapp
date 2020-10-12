package com.chernyshev.weatherloggerapp.presentation.main_screen

import androidx.lifecycle.ViewModel
import com.chernyshev.weatherloggerapp.LocationDisabledException
import com.chernyshev.weatherloggerapp.NetworkDisabledException
import com.chernyshev.weatherloggerapp.domain.contract.DatabaseProvider
import com.chernyshev.weatherloggerapp.domain.contract.WeatherProvider
import com.chernyshev.weatherloggerapp.domain.entity.Weather
import com.chernyshev.weatherloggerapp.presentation.ViewStateHolder
import com.chernyshev.weatherloggerapp.presentation.ViewStateHolderImpl
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val weatherProvider: WeatherProvider,
    private val database: DatabaseProvider
) : ViewModel(),
    ViewStateHolder<MainScreenViewState> by ViewStateHolderImpl() {

    private lateinit var makeInternetDisabledToast: () -> Unit
    private lateinit var makeLocationServicesDisabledToast: () -> Unit
    private lateinit var makeUnknownIssueToast: () -> Unit

    init {
        updateWeather()
        getLastSavedWeather()
    }

    fun updateWeather() {
        GlobalScope.launch {
            try {
                val weather = weatherProvider.getCurrentWeather()
                updateState {
                    MainScreenViewState(
                        weather = weather,
                        it?.lastSaving
                    )
                }
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
    }

    private fun getLastSavedWeather() {
        GlobalScope.launch {
            try {
                val lastSaving = database.getLastSaving()
                updateState {
                    MainScreenViewState(
                        weather = it?.weather,
                        lastSaving = lastSaving
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
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

    fun saveCurrentWeather() {
        database.saveCurrentWeather(viewState.value!!.weather!!)
        getLastSavedWeather()
    }
}

data class MainScreenViewState(
    val weather: Weather?,
    val lastSaving: Weather?
)