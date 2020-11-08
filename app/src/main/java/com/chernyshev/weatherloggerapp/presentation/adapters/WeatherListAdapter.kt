package com.chernyshev.weatherloggerapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.databinding.VWeatherItemBinding
import com.chernyshev.weatherloggerapp.domain.entity.*
import javax.inject.Inject

class WeatherListAdapter @Inject constructor(
    private val context: Context
) :
    RecyclerView.Adapter<WeatherListAdapter.ItemViewHolder>() {

    private var listOfItems: MutableList<Pair<Location, Weather>> = mutableListOf()
    private lateinit var onItemClick: (weather: WeatherViewData) -> Unit
    private lateinit var onLongItemClick: (weather: Pair<Location, Weather>) -> Unit

    fun setOnItemClick(doing: (weather: WeatherViewData) -> Unit) {
        onItemClick = doing
    }

    fun setOnLongItemClick(doing: (weather: Pair<Location, Weather>) -> Unit) {
        onLongItemClick = doing
    }

    fun setItems(items: List<Pair<Location, Weather>>) {
        if (items != listOfItems) {
            listOfItems.clear()
            listOfItems.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            VWeatherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(listOfItems[position])
    }

    override fun getItemCount(): Int = listOfItems.size

    inner class ItemViewHolder(private val binding: VWeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(weather: Pair<Location, Weather>) {
            with(binding) {
                location.text = weather.first.city
                currentTemperature.text =
                    String.format(
                        context.getString(R.string.current_temperature), weather.second.temperature
                    )
                currentTime.text = weather.second.timeStamp.toTime()
                weatherIcon.setImageResource(weather.second.iconId)
                weatherDescription.text = weather.second.description.capitalize()

                root.setOnClickListener {
                    onItemClick(weather.second.toViewData(context))
                }

                root.setOnLongClickListener {
                    onLongItemClick(weather)
                    true
                }
            }
        }
    }
}