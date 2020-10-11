package com.chernyshev.weatherloggerapp.data.providers

import android.util.Log
import com.chernyshev.weatherloggerapp.FirebaseLoginException
import com.chernyshev.weatherloggerapp.FirebaseWritingIException
import com.chernyshev.weatherloggerapp.domain.contract.DatabaseProvider
import com.chernyshev.weatherloggerapp.domain.entity.Weather
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

private const val USERS_COLLECTION = "users"

class DatabaseProviderImpl @Inject constructor() : DatabaseProvider {
//    private lateinit var currentUser: FirebaseUser
//    private val database = Firebase.firestore
//    private var auth = Firebase.auth


//    override suspend fun saveCurrentWeather(weather: Weather) {
//        Log.d("kek", "saveCurrentWeather start")
//        signInAnonymously() ?: throw FirebaseLoginException()
//        Log.d("kek", "saveCurrentWeather signed in anonymously")
//        saveWeather(weather)
//        Log.d("kek", "saveCurrentWeather end")
//    }


    private lateinit var auth: FirebaseAuth
    private var currentUser_: FirebaseUser? = null
    private val database = Firebase.firestore
    override suspend fun saveCurrentWeather(weather: Weather){
        val USERNAME = "username"

        auth = Firebase.auth
        currentUser_ = auth.currentUser

        var database = Firebase.firestore



        database.collection(USERS_COLLECTION)
            .document(currentUser_!!.uid.toString())
            .set(weather)
            .addOnSuccessListener {
                Log.d("kek","USERNAME SET SUCCESSFULLY")
            }
            .addOnFailureListener {
                Log.d("kek","USERNAME SET FAIL")
            }
    }

//    private suspend fun signInAnonymously(): AuthResult? {
//        return try {
//            currentUser = auth.currentUser ?: throw FirebaseLoginException()
//
//            val result = auth
//                .signInAnonymously()
//                .await()
//            result
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//    private suspend fun saveWeather(weather : Weather) {
//        try {
//            Log.d("kek", "saveCurrentWeather saveWeather start")
//            database.collection(USERS_COLLECTION)
//                .document(currentUser.uid)
//                .set(weather)
//                .await()
//            Log.d("kek", "saveCurrentWeather saveWeather end")
//        } catch (e: Exception) {
//            throw FirebaseWritingIException()
//        }
//    }

}