package com.chernyshev.weatherloggerapp.presentation.savings_screen

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.databinding.FSavingsScreenBinding
import com.chernyshev.weatherloggerapp.domain.contract.DatabaseProvider
import com.chernyshev.weatherloggerapp.presentation.more_info_dialog.MoreInfoDialog
import com.chernyshev.weatherloggerapp.presentation.onChangeState
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavingsScreenFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: SavingsListAdapter

    @Inject
    lateinit var database: DatabaseProvider

    private lateinit var viewModel: SavingsScreenViewModel
    private lateinit var viewBinding: FSavingsScreenBinding


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FSavingsScreenBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[SavingsScreenViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            showLoadingAnimation()

            allSavingsRecycler.adapter = adapter
            setNavigateToShowMoreInfo()
            setOnLongItemClick()
        }

        onChangeState(viewModel) {
            hideLoadingAnimation()
            adapter.setItems(viewModel.viewState.value!!.weathersList)
        }
    }

    private fun showLoadingAnimation() {
        with(viewBinding) {
            loadingAnimation.it.visibility = View.VISIBLE
        }
    }

    private fun hideLoadingAnimation() {
        with(viewBinding) {
            if (loadingAnimation.it.visibility == View.VISIBLE)
                loadingAnimation.it.visibility = View.INVISIBLE
        }
    }

    private fun setNavigateToShowMoreInfo() {
        adapter.setNavigateToShowMoreInfo { weather ->
            findNavController().navigate(
                R.id.action_savingsScreen_to_moreInfoDialog,
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
    }

    private fun setOnLongItemClick() {
        adapter.setRemoveItem {
            showConfirmationDialog {
                GlobalScope.launch {
                    database.removeWeather(it.timeStamp)
                    viewModel.updateSavingsList()
                }
            }
        }
    }

    private fun showConfirmationDialog(onConfirm: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(getString(R.string.delete_saving_question))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                onConfirm()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}