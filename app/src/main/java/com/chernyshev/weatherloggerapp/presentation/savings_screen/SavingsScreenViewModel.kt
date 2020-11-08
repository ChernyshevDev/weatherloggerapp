package com.chernyshev.weatherloggerapp.presentation.savings_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.chernyshev.weatherloggerapp.domain.contract.DatabaseProvider
import com.chernyshev.weatherloggerapp.domain.entity.WeatherViewData
import com.chernyshev.weatherloggerapp.domain.entity.toViewData
import com.chernyshev.weatherloggerapp.presentation.ViewStateHolder
import com.chernyshev.weatherloggerapp.presentation.ViewStateHolderImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavingsScreenViewModel @Inject constructor(
    private val database: DatabaseProvider,
    private val context: Context
) : ViewModel(),
    ViewStateHolder<SavingsScreenViewState> by ViewStateHolderImpl() {

    init {
        updateSavingsList()
    }

    fun updateSavingsList() {
        GlobalScope.launch {
            val weatherList = database.getAllSavings()
            updateState {
                SavingsScreenViewState(
                    weathersList = weatherList
                        .sortedByDescending { it.timeStamp }
                        .map { it.toViewData(context) }
                )
            }
        }
    }
}

data class SavingsScreenViewState(
    val weathersList: List<WeatherViewData>
)
