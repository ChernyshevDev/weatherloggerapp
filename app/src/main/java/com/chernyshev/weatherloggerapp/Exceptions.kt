package com.chernyshev.weatherloggerapp

class NetworkDisabledException : Exception("Internet on device is disabled")

class LocationDisabledException : Exception("Device location services are disabled")

class FirebaseLoginException : Exception("Error while log in to firebase")

class FirebaseWritingIException : Exception("Error saving weather in to firebase")

