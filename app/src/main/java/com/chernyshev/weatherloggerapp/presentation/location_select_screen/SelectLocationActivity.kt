package com.chernyshev.weatherloggerapp.presentation.location_select_screen

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.databinding.ActivitySelectLocationBinding
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import dagger.android.AndroidInjection
import javax.inject.Inject

class SelectLocationActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: SelectLocationViewModel

    private lateinit var viewBinding: ActivitySelectLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySelectLocationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[SelectLocationViewModel::class.java]

        setupInputField()
    }

    private fun setupInputField() {
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES)
        autocompleteFragment.setPlaceFields(listOf(Place.Field.NAME, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                viewBinding.selectCityHeader.visibility = View.INVISIBLE
                showSaveConfirmationDialog(place)
            }

            override fun onError(p0: Status) {
            }
        })
    }

    private fun showSaveConfirmationDialog(place: Place) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.confirm_saving, place.name))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.savePlace(place)
                Toast.makeText(
                    this@SelectLocationActivity,
                    getString(R.string.saved),
                    Toast.LENGTH_SHORT
                ).show()
                this@SelectLocationActivity.finish()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }
}