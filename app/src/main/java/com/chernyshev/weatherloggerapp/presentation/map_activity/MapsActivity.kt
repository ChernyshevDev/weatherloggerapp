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
        setupBottomSheet()
        setupOnMapClick()
    }

    private fun setupOnMapClick() {
        map.setOnMapClickListener {
            removeCurrentSelectionMarker()
            showLocationInfo(viewModel.toCoordinates(it))
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

    private fun showLocationInfo(coordinates: Coordinates) {
        GlobalScope.launch(Dispatchers.Main) {
            val location = viewModel.toLocation(coordinates)
            with(viewBinding.bottomSheet) {
                cityName.text = location.city
                countryName.text = location.country

                var items: List<Info>? = null
                withContext(Dispatchers.IO) {
                    items = viewModel.getWeatherDescriptionItems(coordinates)
                }
                adapter.setItems(items!!)
                weatherDescriptionRecycler.adapter = adapter

                saveLocationButton.setOnClickListener {
                    viewModel.saveLocation(location)
                }
            }
            expandBottomSheet()
        }
    }

    private fun expandBottomSheet() {
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
    }
}