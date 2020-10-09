package com.example.weatherloggerapp

class NetworkDisabledException : Exception("Internet on device is disabled")

class LocationDisabledException : Exception("Device location services are disabled")
