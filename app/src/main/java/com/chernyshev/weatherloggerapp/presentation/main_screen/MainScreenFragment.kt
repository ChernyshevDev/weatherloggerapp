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
import kotlinx.android.synthetic.main.v_last_saving.*
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

        onChangeState(viewModel) {
            bindTextViews()
        }

        setErrorToasters()
        bindButtons()

    }

    private fun setErrorToasters() {
        viewModel.setInternetDisabledToast {
            Toast.makeText(
                context,
                "Cannot update weather: Internet disabled on device",
                Toast.LENGTH_LONG
            ).show()
        }
        viewModel.setLocationDisabledToast {
            Toast.makeText(
                context,
                "Cannot update weather: Location disabled on device",
                Toast.LENGTH_LONG
            ).show()
        }
        viewModel.setUnknownIssueToast {
            Toast.makeText(
                context,
                "Cannot update weather: Unknown issue. Please try again later.",
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

    private fun navigateToSavingsScreen(){
        findNavController().navigate(R.id.action_mainScreen_to_savingsScreen)
    }


    private fun bindTextViews() {
        with(viewBinding) {
            with(viewModel.viewState.value!!.weather!!) {
                mainScreenCurrentLocation.text = city
                weatherNow.weatherNowCurrentTemperature.text =
                    String.format(
                        resources.getString(R.string.current_temperature), temperature
                    )
                weatherNow.weatherNowCurrentTime.text = timeStamp.toTime()
                weatherNow.weatherNowCurrentWeatherIcon.setImageResource(iconId)
                weatherNow.weatherNowWeatherDescription.text = description.capitalize()
            }


            viewModel.viewState.value?.lastSaving?.let{
                lastSaving.lastSavingCity.text = it.city
                lastSaving.lastSavingDate.text = it.timeStamp.toDate()
                lastSaving.lastSavingTime.text = it.timeStamp.toTime()
                lastSaving.lastSavingWeatherDescription.text = it.description
            }

            }
        }
    }