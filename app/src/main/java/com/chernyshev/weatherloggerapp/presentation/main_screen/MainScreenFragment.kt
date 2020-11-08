package com.chernyshev.weatherloggerapp.presentation.main_screen

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.databinding.FMainScreenBinding
import com.chernyshev.weatherloggerapp.domain.entity.Location
import com.chernyshev.weatherloggerapp.domain.entity.WeatherViewData
import com.chernyshev.weatherloggerapp.domain.entity.toTime
import com.chernyshev.weatherloggerapp.domain.entity.toViewData
import com.chernyshev.weatherloggerapp.presentation.adapters.WeatherListAdapter
import com.chernyshev.weatherloggerapp.presentation.map_activity.MapsActivity
import com.chernyshev.weatherloggerapp.presentation.more_info_dialog.MoreInfoDialog
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.*
import javax.inject.Inject

class MainScreenFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var viewBinding: FMainScreenBinding

    @Inject
    lateinit var adapter: WeatherListAdapter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FMainScreenBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[MainScreenViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)

        showLoadingAnimation()
        setErrorToasters()
        setupButtons()
        setupRecyclerView()

        viewModel.weatherInCurrentLocation.observe(viewLifecycleOwner) {
            runBlocking {
                if (loadingAnimationIsNotVisible()) {
                    /**
                     * For better user experience lets pretend
                     * that app is performing something
                     */
                    showLoadingAnimationsForAWhile()
                }
                bindWeatherNow()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        GlobalScope.launch {
            viewModel.updateOtherCitiesWeathers()
        }
    }

    private suspend fun showLoadingAnimationsForAWhile() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                showLoadingAnimation()
            }

            delay(1500)

            withContext(Dispatchers.Main) {
                hideLoadingAnimation()
            }
        }
    }

    private fun showLoadingAnimation() {
        with(viewBinding) {
            weatherNow.loadingAnimation.it.visibility = View.VISIBLE
        }
    }

    private fun hideLoadingAnimation() {
        with(viewBinding) {
            weatherNow.loadingAnimation.it.visibility = View.INVISIBLE
        }
    }

    private fun setupButtons() {
        with(viewBinding) {
            mainScreenRefreshButton.setOnClickListener {
                GlobalScope.launch {
                    viewModel.updateWeatherInCurrentLocation()
                }
            }
            mainScreenSaveButton.setOnClickListener {
                viewModel.saveCurrentWeather()
            }
            mainScreenAllSavingsButton.setOnClickListener {
                navigateToSavingsScreen()
            }
            mainScreenLocationsButton.setOnClickListener {
                navigateToMapScreen()
            }
        }
    }

    private fun navigateToMapScreen() {
        startActivity(Intent(requireContext(), MapsActivity::class.java))
    }

    private fun navigateToSavingsScreen() {
        findNavController().navigate(R.id.action_mainScreen_to_savingsScreen)
    }

    private fun bindWeatherNow() {
        with(viewBinding) {
            viewModel.weatherInCurrentLocation.value?.let { weather ->
                mainScreenCurrentLocation.text = weather.city
                weatherNow.location.text = weather.city
                weatherNow.currentTemperature.text =
                    String.format(
                        getString(R.string.current_temperature), weather.temperature
                    )
                weatherNow.currentTime.text = weather.timeStamp.toTime()
                weatherNow.weatherIcon.setImageResource(weather.iconId)
                weatherNow.weatherDescription.text = weather.description.capitalize()

                weatherNow.loadingAnimation.it.visibility = View.INVISIBLE

                root.setOnClickListener {
                    openExpandedInfoDialog(weather.toViewData())
                }
            }
        }
    }

    private fun setupRecyclerView() {
        viewModel.otherCitiesWeathers.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }

        adapter.setOnItemClick {
            openExpandedInfoDialog(it)
        }

        adapter.setOnLongItemClick {
            showRemoveSavingDialog(it.first)
        }

        viewBinding.otherCitiesWeatherRecycler.adapter = adapter
    }

    private fun openExpandedInfoDialog(weather: WeatherViewData) {
        findNavController().navigate(
            R.id.action_mainScreenFragment_to_moreInfoDialog,
            Bundle().apply {
                putString(MoreInfoDialog.TEMPERATURE, weather.temperature)
                putString(MoreInfoDialog.CITY, weather.city)
                putString(MoreInfoDialog.DATE, weather.date)
                putString(MoreInfoDialog.TIME, weather.time)
                putString(MoreInfoDialog.DESCRIPTION, weather.description)
                putString(MoreInfoDialog.PRESSURE, weather.pressure)
                putString(MoreInfoDialog.WIND_SPEED, weather.windSpeed)
            })
    }

    private fun showRemoveSavingDialog(location: Location) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(getString(R.string.delete_saving_question))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteSavedLocation(location)
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun setErrorToasters() {
        viewModel.setInternetDisabledToast {
            Toast.makeText(
                context,
                getString(R.string.internet_disabled),
                Toast.LENGTH_LONG
            ).show()
        }
        viewModel.setLocationDisabledToast {
            Toast.makeText(
                context,
                getString(R.string.location_disabled),
                Toast.LENGTH_LONG
            ).show()
        }
        viewModel.setUnknownIssueToast {
            Toast.makeText(
                context,
                getString(R.string.cant_update_weather_unknown_issue),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun loadingAnimationIsNotVisible(): Boolean {
        with(viewBinding) {
            return weatherNow.loadingAnimation.it.visibility == View.INVISIBLE
        }
    }

}