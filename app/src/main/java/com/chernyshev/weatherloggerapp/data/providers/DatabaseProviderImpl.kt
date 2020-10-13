package com.chernyshev.weatherloggerapp.data.providers

import android.content.Context
import android.widget.Toast
import com.chernyshev.weatherloggerapp.R
import com.chernyshev.weatherloggerapp.domain.contract.DatabaseProvider
import com.chernyshev.weatherloggerapp.domain.entity.Weather
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val TIMESTAMP = "timeStamp"
const val TEMPERATURE = "temperature"
const val CITY = "city"
const val DESCRIPTION = "description"
const val PRESSURE = "pressure"
const val WIND_SPEED = "windSpeed"

class DatabaseProviderImpl @Inject constructor(
    private val context: Context
) : DatabaseProvider {

    private var auth: FirebaseAuth = Firebase.auth
    private var currentUser: FirebaseUser? = null
    private lateinit var uid: String
    private lateinit var database: FirebaseFirestore

    init {
        runBlocking {
            signInAnonymously()
        }
    }

    private suspend fun signInAnonymously() {
        withContext(Dispatchers.Default) {
            auth.signInAnonymously().await()
            currentUser = auth.currentUser
            uid = currentUser!!.uid
            database = Firebase.firestore
        }
    }

    override fun saveCurrentWeather(weather: Weather) {
        database.collection(uid)
            .add(weather)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    context.getString(R.string.success),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override suspend fun removeWeather(id: Long) {
        withContext(Dispatchers.Default) {
            val documents = database.collection(uid)
                .get()
                .await()
            val documentName = documents
                .find { it[TIMESTAMP] == id }
                ?.reference?.id.toString()

            database.collection(uid)
                .document(documentName)
                .delete()
        }
    }

    override suspend fun getLastSaving(): Weather {
        val allSavings = getAllSavings()
            .sortedByDescending { it.timeStamp }

        return allSavings[0]
    }

    override suspend fun getAllSavings(): List<Weather> {
        val weathers = database.collection(uid)
            .get()
            .await()

        val listOfWeathers = mutableListOf<Weather>()

        for (weather in weathers) {
            listOfWeathers.add(
                Weather(
                    temperature = (weather[TEMPERATURE].toString()).toInt(),
                    city = weather[CITY].toString(),
                    timeStamp = (weather[TIMESTAMP].toString()).toLong(),
                    description = weather[DESCRIPTION].toString(),
                    pressure = (weather[PRESSURE].toString()).toInt(),
                    windSpeed = (weather[WIND_SPEED].toString()).toDouble()
                )
            )
        }

        return listOfWeathers
    }
}