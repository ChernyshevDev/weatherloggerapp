package com.chernyshev.weatherloggerapp.presentation.map_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.data.providers.AddressProviderImpl
import com.chernyshev.weatherloggerapp.databinding.ActivityMapsBinding
import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.chernyshev.weatherloggerapp.domain.entity.Location
import com.chernyshev.weatherloggerapp.presentation.main_screen.MainScreenViewModel
import com.chernyshev.weatherloggerapp.presentation.more_info_dialog.MoreInfoAdapter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MapsActivityViewModel

    @Inject
    lateinit var adapter : MoreInfoAdapter

    private lateinit var map: GoogleMap
    private lateinit var viewBinding: ActivityMapsBinding

    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[MapsActivityViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
}

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setupBottomSheet()
        setupOnMapClick()

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun setupOnMapClick() {
        map.setOnMapClickListener {
            showBottomSheet(it.toCoordinates())
        }
    }

    private fun setupBottomSheet() {
        val bottomSheet = viewBinding.bottomSheetContainer
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)

        bottomSheet.isClickable = true
        bottomSheetBehaviour.isDraggable = true
        bottomSheetBehaviour.isHideable = true
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showBottomSheet(coordinates: Coordinates) {
        val location = coordinates.toLocation()
        with(viewBinding.bottomSheet){
            cityName.text = location.city
            countryName.text = location.country

            val items = viewModel.getWeatherDescriptionItems()
            adapter.setItems(items)
            weatherDescriptionRecycler.adapter = adapter

            saveLocationButton.setOnClickListener {
                viewModel.saveLocation(location)
            }
        }
        expandBottomSheet()
    }

    private fun expandBottomSheet() {
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun Coordinates.toLocation() : Location = AddressProviderImpl(applicationContext).getAddress(this)
    private fun LatLng.toCoordinates() : Coordinates {
        return Coordinates(
            latitude = this.latitude,
            longitude = this.longitude
        )
    }
}