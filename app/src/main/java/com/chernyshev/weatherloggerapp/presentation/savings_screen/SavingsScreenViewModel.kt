package com.chernyshev.weatherloggerapp.presentation.savings_screen

import androidx.lifecycle.ViewModel
import com.chernyshev.weatherloggerapp.domain.contract.DatabaseProvider
import com.chernyshev.weatherloggerapp.domain.entity.Weather
import com.chernyshev.weatherloggerapp.presentation.ViewStateHolder
import com.chernyshev.weatherloggerapp.presentation.ViewStateHolderImpl
import javax.inject.Inject

class SavingsScreenViewModel @Inject constructor(
    private val database: DatabaseProvider
) : ViewModel(),
    ViewStateHolder<SavingsScreenViewState> by ViewStateHolderImpl() {

}

data class SavingsScreenViewState(
    val weathersList: List<Weather>
)