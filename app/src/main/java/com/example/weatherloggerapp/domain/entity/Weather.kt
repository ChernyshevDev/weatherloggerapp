package com.example.weatherloggerapp.domain.entity

data class Weather(
    val temperature: Float,
    val city: String,
    val dateTime: String,
    val longitude: Double,
    val latitude: Double
)