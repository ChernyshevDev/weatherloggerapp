package com.example.weatherloggerapp.presentation.main_screen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.weatherloggerapp.R
import com.example.weatherloggerapp.presentation.onChangeState
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MainScreenFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainScreenViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.f_main_screen, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[MainScreenViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)

        onChangeState(viewModel) {
            setCurrentWeather()
        }
    }

    private fun setCurrentWeather() {
        with(viewModel.viewState.value!!.weather) {
            Log.d("kek", "---------- current weather ------------")
            Log.d("kek", "time: $dateTime")
            Log.d("kek", "temperature: $temperature")
            Log.d("kek", "long: $longitude")
            Log.d("kek", "lat: $latitude")
            Log.d("kek", "city: $city")
        }
    }
}