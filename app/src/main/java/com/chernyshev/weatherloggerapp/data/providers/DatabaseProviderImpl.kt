package com.chernyshev.weatherloggerapp.data.providers

import android.util.Log
import com.chernyshev.weatherloggerapp.domain.contract.DatabaseProvider
import com.chernyshev.weatherloggerapp.domain.entity.Weather
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DatabaseProviderImpl @Inject constructor() : DatabaseProvider {

    var auth: FirebaseAuth = Firebase.auth
    private var currentUser: FirebaseUser? = null

    init {
        signInAnonymously()
    }

    private fun signInAnonymously() {
        GlobalScope.launch(Dispatchers.Main)
        {
            auth.signInAnonymously().await()
            currentUser = auth.currentUser
            Log.d("kek", "current uid -> ${currentUser!!.uid}")
        }
    }

    override suspend fun saveCurrentWeather(weather: Weather) {
        val db = Firebase.firestore

        db.collection(currentUser!!.uid)
            .add(weather)
            .addOnSuccessListener {
                Log.d("kek", "SUCCESS WRITING TO DATABASE")
            }
            .addOnFailureListener {
                Log.d("kek", "FAIL WRITING TO DATABASE")
            }

    }
}