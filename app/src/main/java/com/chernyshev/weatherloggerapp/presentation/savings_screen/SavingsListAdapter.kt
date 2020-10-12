package com.chernyshev.weatherloggerapp.presentation.savings_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chernyshev.weatherloggerapp.databinding.VSavingBinding
import com.chernyshev.weatherloggerapp.domain.entity.WeatherViewData
import javax.inject.Inject

class SavingsListAdapter @Inject constructor() :
    RecyclerView.Adapter<SavingsListAdapter.ItemViewHolder>() {

    private var weatherSavings: List<WeatherViewData> = listOf()
    private lateinit var navigateToShowMoreInfo: (weather: WeatherViewData) -> Unit
    private lateinit var removeItem: (weather: WeatherViewData) -> Unit

    fun setItems(items: List<WeatherViewData>) {
        weatherSavings = items
        notifyDataSetChanged()
    }

    fun setNavigateToShowMoreInfo(doing: (weather: WeatherViewData) -> Unit) {
        navigateToShowMoreInfo = doing
    }

    fun setRemoveItem(doing: (weather: WeatherViewData) -> Unit) {
        removeItem = doing
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            VSavingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindViewData(weatherSavings[position])
    }

    override fun getItemCount(): Int = weatherSavings.size

    inner class ItemViewHolder(private val binding: VSavingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViewData(weather: WeatherViewData) {
            with(binding) {
                savedWeatherDate.text = weather.date
                savedWeatherTime.text = weather.time

                savedWeather.setOnClickListener {
                    showMoreInfo(weather)
                }

                savedWeather.setOnLongClickListener {
                    removeItem(weather)
                    true
                }
            }
        }

        private fun showMoreInfo(weather: WeatherViewData) {
            navigateToShowMoreInfo(weather)
        }
    }
}