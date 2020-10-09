package com.example.weatherloggerapp.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherloggerapp.MainActivity
import com.example.weatherloggerapp.data.providers.CoordinatesProviderImpl
import com.example.weatherloggerapp.data.providers.WeatherProviderImpl
import com.example.weatherloggerapp.domain.contract.CoordinatesProvider
import com.example.weatherloggerapp.domain.contract.WeatherProvider
import com.example.weatherloggerapp.presentation.main_screen.MainScreenFragment
import com.example.weatherloggerapp.presentation.main_screen.MainScreenViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
internal class AppModule {
    @Provides
    fun provideWeatherProvider(
        weatherProviderImpl: WeatherProviderImpl
    ): WeatherProvider = weatherProviderImpl

    @Provides
    fun provideCoordinatesProvider(
        coordinatesProviderImpl: CoordinatesProviderImpl
    ): CoordinatesProvider = coordinatesProviderImpl
}

@Module
abstract class ActivityModule {
    @Binds
    internal abstract fun appContext(app: Application): Context

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity
}

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMainScreenFragment(): MainScreenFragment
}

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    internal abstract fun bindMainScreenViewModel(viewModel: MainScreenViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)