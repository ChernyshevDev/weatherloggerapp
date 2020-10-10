package com.example.weatherloggerapp.domain.entity

data class Weather(
    val temperature: Int,
    val city: String,
    val dateTime: DateTime,
    val longitude: Double,
    val latitude: Double,
    val description: String,
    val iconId: Int
)

data class DateTime(
    val date: String,
    val time: String
)