package com.example.weatherloggerapp.di

import android.app.Application
import com.example.weatherloggerapp.WeatherLoggerApp
import com.example.weatherloggerapp.presentation.main_screen.MainScreenViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(target: WeatherLoggerApp)
    fun inject(target: MainScreenViewModel)
}