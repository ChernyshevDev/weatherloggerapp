package com.chernyshev.weatherloggerapp.presentation.location_select_screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.chernyshev.weatherloggerapp.R
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class SelectLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_location)

        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES)
        autocompleteFragment.setPlaceFields(listOf(Place.Field.NAME))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onPlaceSelected(place: Place) {
                Toast.makeText(applicationContext,place.name + " selected!", Toast.LENGTH_SHORT).show()
            }

            override fun onError(p0: Status) {

            }

        })
    }
}