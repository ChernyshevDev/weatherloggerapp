package com.chernyshev.weatherloggerapp.presentation.map_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.databinding.ActivityMapsBinding
import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.chernyshev.weatherloggerapp.domain.entity.Info
import com.chernyshev.weatherloggerapp.domain.entity.Location
import com.chernyshev.weatherloggerapp.presentation.adapters.InfoListAdapter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.AndroidInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var adapter: InfoListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MapsActivityViewModel

    private lateinit var map: GoogleMap
    private lateinit var viewBinding: ActivityMapsBinding
    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
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
        updateSavedLocationsMarkers()
        setupBottomSheet()
        setupOnMapClick()
        setupOnMarkerClick()
    }

    private fun setupOnMarkerClick() {
        map.setOnMarkerClickListener { clickedMarker ->
            for ((marker, location) in viewModel.savedLocationsMarkers) {
                if (clickedMarker == marker) {
                    GlobalScope.launch {
                        showLocationInfoBottomSheet(location.coordinates, isSaved = true)
                    }
                    true
                }
            }
            false
        }
    }

    private fun updateSavedLocationsMarkers() {
        for (location in viewModel.savedLocations) {
            val marker = map.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            location.coordinates.latitude,
                            location.coordinates.longitude
                        )
                    )
            )
            viewModel.savedLocationsMarkers.add(Pair(marker, location))
        }
    }

    private fun setupOnMapClick() {
        map.setOnMapClickListener {
            removeCurrentSelectionMarker()
            GlobalScope.launch {
                showLocationInfoBottomSheet(viewModel.toCoordinates(it))
            }
            setCurrentSelectionMarker(it)
        }
    }

    private fun setCurrentSelectionMarker(coordinates: LatLng) {
        viewModel.currentSelectionMarker = map.addMarker(MarkerOptions().position(coordinates))
    }

    private fun removeCurrentSelectionMarker() {
        viewModel.currentSelectionMarker?.remove()
    }

    private fun setupBottomSheet() {
        val bottomSheet = viewBinding.bottomSheetContainer
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet)

        bottomSheet.isClickable = true
        bottomSheetBehaviour.isDraggable = true
        bottomSheetBehaviour.isHideable = true
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehaviour.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    removeCurrentSelectionMarker()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private suspend fun showLocationInfoBottomSheet(
        coordinates: Coordinates,
        isSaved: Boolean = false
    ) {
        withContext(Dispatchers.Main) {
            val location = viewModel.toLocation(coordinates)
            setupTextViews(location)
            setupButton(location, isSaved)
            expandBottomSheet()
        }
    }

    private fun setupButton(location: Location, isSaved: Boolean) {
        with(viewBinding.bottomSheet) {
            if (isSaved) {
                saveLocationButton.visibility = View.GONE
                removeLocationButton.visibility = View.VISIBLE

                removeLocationButton.setOnClickListener {
                    removeMarkerAt(location.coordinates)
                    viewModel.removeLocationSaving(location)
                }
            } else {
                saveLocationButton.visibility = View.VISIBLE
                removeLocationButton.visibility = View.GONE

                saveLocationButton.setOnClickListener {
                    viewModel.saveLocation(location)
                    updateSavedLocationsMarkers()
                }
            }
        }
    }

    private suspend fun setupTextViews(location: Location) {
        with(viewBinding.bottomSheet) {
            cityName.text = location.city
            countryName.text = location.country

            var items: List<Info>?
            withContext(Dispatchers.IO) {
                items = viewModel.getWeatherDescriptionItems(location.coordinates)
            }
            adapter.setItems(items!!)
            weatherDescriptionRecycler.adapter = adapter
        }
    }

    private fun expandBottomSheet() {
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun removeMarkerAt(coordinates: Coordinates) {
        for ((marker, location) in viewModel.savedLocationsMarkers) {
            if (location.coordinates == coordinates) {
                marker.remove()
                return
            }
        }
    }
}