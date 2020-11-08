package com.chernyshev.weatherloggerapp.data.providers

import android.content.Context
import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.data.retrofit.WeatherResponse
import com.chernyshev.weatherloggerapp.data.retrofit.WeatherService
import com.chernyshev.weatherloggerapp.data.retrofit.openWeatherMapBaseUrl
import com.chernyshev.weatherloggerapp.data.retrofit.toWeather
import com.chernyshev.weatherloggerapp.domain.contract.CoordinatesProvider
import com.chernyshev.weatherloggerapp.domain.contract.WeatherProvider
import com.chernyshev.weatherloggerapp.domain.entity.Coordinates
import com.chernyshev.weatherloggerapp.domain.entity.Weather
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class WeatherProviderImpl @Inject constructor(
    private val coordinatesProvider: CoordinatesProvider,
    private val context: Context
) : WeatherProvider {

    override suspend fun getLocalWeather(): Weather {
        val coordinates = coordinatesProvider.getCurrentCoordinates()
        val weatherRequest = getWeatherRequestCall(coordinates = coordinates)
        val responseFromServer = weatherRequest.makeRequest()

        return responseFromServer!!.toWeather().localize()
    }

    override suspend fun getWeatherIn(city: String): Weather {
        val weatherRequest = getWeatherRequestCall(city = city)
        val responseFromServer = weatherRequest.makeRequest()

        return responseFromServer!!.toWeather().localize()
    }

    override suspend fun getWeatherIn(coordinates: Coordinates): Weather {
        val weatherRequest = getWeatherRequestCall(coordinates)
        val responseFromServer = weatherRequest.makeRequest()

        return responseFromServer!!.toWeather().localize()
    }

    private fun getWeatherRequestCall(
        coordinates: Coordinates? = null,
        city: String? = null
    ): Call<WeatherResponse> {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(openWeatherMapBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherService: WeatherService = retrofit.create(WeatherService::class.java)
        val language = context.resources.configuration.locales[0].language

        return if (coordinates != null) {
            weatherService.getWeather(
                lat = coordinates.latitude,
                lon = coordinates.longitude,
                lang = language
            )
        } else weatherService.getWeather(city = city!!, lang = language)
    }

    private fun Call<WeatherResponse>.makeRequest() =
        this.execute()
            .body()

    private fun Weather.localize(): Weather {
        return when (this.description) {
            "clear sky" -> this.apply { description = context.getString(R.string.clear_sky) }
            "few clouds" -> this.apply { description = context.getString(R.string.few_clouds) }
            "scattered clouds" -> this.apply {
                description = context.getString(R.string.scattered_clouds)
            }
            "broken clouds" -> this.apply {
                description = context.getString(R.string.broken_clouds)
            }
            "shower rain" -> this.apply { description = context.getString(R.string.shower_rain) }
            "rain" -> this.apply { description = context.getString(R.string.rain) }
            "thunderstorm" -> this.apply { description = context.getString(R.string.thunderstorm) }
            "snow" -> this.apply { description = context.getString(R.string.snow) }
            "mist" -> this.apply { description = context.getString(R.string.mist) }
            else -> this.apply { description = context.getString(R.string.scattered_clouds) }
        }
    }
}