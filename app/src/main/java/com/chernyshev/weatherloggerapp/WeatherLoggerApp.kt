package com.chernyshev.weatherloggerapp

import android.app.Application
import com.chernyshev.weatherloggerapp.di.DaggerAppComponent
import com.google.android.libraries.places.api.Places
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.realm.Realm
import javax.inject.Inject

class WeatherLoggerApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        Realm.init(applicationContext)

        Places.initialize(applicationContext,getString(R.string.place_autocomplete_api_key))
        Places.createClient(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}