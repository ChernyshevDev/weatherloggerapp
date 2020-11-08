package com.chernyshev.weatherloggerapp

import android.app.Application
import com.chernyshev.weatherloggerapp.di.DaggerAppComponent
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
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}