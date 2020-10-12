package com.chernyshev.weatherloggerapp.presentation.main_screen

import android.content.Context
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
import com.chernyshev.weatherloggerapp.domain.entity.toDate
import com.chernyshev.weatherloggerapp.domain.entity.toTime
import com.chernyshev.weatherloggerapp.presentation.onChangeState
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.*
import javax.inject.Inject

class MainScreenFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainScreenViewModel
    private lateinit var viewBinding: FMainScreenBinding

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

        showLoadingAnimations()

        onChangeState(viewModel) {
            runBlocking {
                if (loadingAnimationsIsNowShowing()) {
                    /**
                     * For better user experience lets pretend
                     * that app is performing something
                     */
                    showLoadingAnimationsForAWhile()
                }
                bindTextViews()
            }
        }

        setErrorToasters()
        bindButtons()
    }

    private fun loadingAnimationsIsNowShowing(): Boolean {
        with(viewBinding) {
            return weatherNow.loadingAnimation.it.visibility == View.INVISIBLE &&
                    lastSaving.loadingAnimation.it.visibility == View.INVISIBLE
        }
    }

    private suspend fun showLoadingAnimationsForAWhile() {
        GlobalScope.launch {
            with(viewBinding) {
                withContext(Dispatchers.Main) {
                    showLoadingAnimations()
                }

                delay(1500)

                withContext(Dispatchers.Main) {
                    hideLoadingAnimations()
                }
            }
        }
    }

    private fun showLoadingAnimations() {
        with(viewBinding) {
            weatherNow.loadingAnimation.it.visibility = View.VISIBLE
            lastSaving.loadingAnimation.it.visibility = View.VISIBLE
        }
    }

    private fun hideLoadingAnimations() {
        with(viewBinding) {
            weatherNow.loadingAnimation.it.visibility = View.INVISIBLE
            lastSaving.loadingAnimation.it.visibility = View.INVISIBLE
        }
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

    private fun bindButtons() {
        with(viewBinding) {
            mainScreenRefreshButton.setOnClickListener {
                viewModel.updateWeather()
            }
            mainScreenSaveButton.setOnClickListener {
                viewModel.saveCurrentWeather()
            }
            mainScreenAllSavingsButton.setOnClickListener {
                navigateToSavingsScreen()
            }
        }
    }

    private fun navigateToSavingsScreen() {
        findNavController().navigate(R.id.action_mainScreen_to_savingsScreen)
    }

    private fun bindTextViews() {
        with(viewBinding) {
            viewModel.viewState.value?.weather?.let {
                mainScreenCurrentLocation.text = it.city
                weatherNow.weatherNowCurrentTemperature.text =
                    String.format(
                        getString(R.string.current_temperature), it.temperature
                    )
                weatherNow.weatherNowCurrentTime.text = it.timeStamp.toTime()
                weatherNow.weatherNowCurrentWeatherIcon.setImageResource(it.iconId)
                weatherNow.weatherNowWeatherDescription.text = it.description.capitalize()

                weatherNow.loadingAnimation.it.visibility = View.INVISIBLE
            }

            viewModel.viewState.value?.lastSaving?.let {
                lastSaving.lastSavingCity.text = it.city
                lastSaving.lastSavingDate.text = it.timeStamp.toDate()
                lastSaving.lastSavingTime.text = it.timeStamp.toTime()
                lastSaving.lastSavingWeatherDescription.text =
                    String.format(
                        getString(
                            R.string.last_saving_weather_description,
                            it.city, it.temperature
                        )
                    )
                lastSaving.loadingAnimation.it.visibility = View.INVISIBLE
            }
        }
    }
}