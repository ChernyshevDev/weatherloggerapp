package com.chernyshev.weatherloggerapp.data.providers

import android.content.Context
import android.widget.Toast
import com.chernyshev.weatherloggerapp.domain.contract.DatabaseProvider
import com.chernyshev.weatherloggerapp.domain.entity.Weather
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseProviderImpl @Inject constructor(
    private val context: Context
) : DatabaseProvider {

    var auth: FirebaseAuth = Firebase.auth
    private var currentUser: FirebaseUser? = null
    private lateinit var uid : String

    init {
        runBlocking {
            signInAnonymously()
        }
    }

    private suspend fun signInAnonymously(){
            withContext(Dispatchers.Default) {
                auth.signInAnonymously().await()
                currentUser = auth.currentUser
                uid = currentUser!!.uid
            }
    }

    override fun saveCurrentWeather(weather: Weather) {
        val db = Firebase.firestore

        db.collection(uid)
            .add(weather)
            .addOnSuccessListener {
                Toast.makeText(context,"Success!",Toast.LENGTH_SHORT).show()
            }
    }

    override suspend fun getLastSaving(): Weather {
        val allSavings = getAllSavings()
            .sortedByDescending { it.timeStamp }


        return allSavings[0]
    }

    override suspend fun getAllSavings(): List<Weather> {
        val db = Firebase.firestore

        val weathers = db.collection(uid)
            .get()
            .await()

        val listOfWeathers = mutableListOf<Weather>()



        for (weather in weathers) {
            listOfWeathers.add(
                Weather(
                    temperature = (weather["temperature"].toString()).toInt(),
                    city = weather["city"].toString(),
                    timeStamp = (weather["timeStamp"].toString()).toLong(),
                    longitude = (weather["longitude"].toString()).toDouble(),
                    latitude = (weather["latitude"].toString()).toDouble(),
                    description = weather["description"].toString(),
                    iconId = (weather["iconId"].toString()).toInt(),
                    pressure = (weather["pressure"].toString()).toInt(),
                    windSpeed = (weather["windSpeed"].toString()).toDouble()
                )
            )

        }

        return listOfWeathers
    }
}