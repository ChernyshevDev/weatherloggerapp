package com.chernyshev.weatherloggerapp.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chernyshev.weatherloggerapp.MainActivity
import com.chernyshev.weatherloggerapp.data.providers.CoordinatesProviderImpl
import com.chernyshev.weatherloggerapp.data.providers.DatabaseProviderImpl
import com.chernyshev.weatherloggerapp.data.providers.WeatherProviderImpl
import com.chernyshev.weatherloggerapp.domain.contract.CoordinatesProvider
import com.chernyshev.weatherloggerapp.domain.contract.DatabaseProvider
import com.chernyshev.weatherloggerapp.domain.contract.WeatherProvider
import com.chernyshev.weatherloggerapp.presentation.main_screen.MainScreenFragment
import com.chernyshev.weatherloggerapp.presentation.main_screen.MainScreenViewModel
import com.chernyshev.weatherloggerapp.presentation.savings_screen.SavingsScreenFragment
import com.chernyshev.weatherloggerapp.presentation.savings_screen.SavingsScreenViewModel
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

    @Provides
    fun provideDatabaseProvider(
        databaseProviderImpl: DatabaseProviderImpl
    ) : DatabaseProvider = databaseProviderImpl
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

    @ContributesAndroidInjector
    internal abstract fun contributesSavingsScreenFragment(): SavingsScreenFragment
}

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    internal abstract fun bindMainScreenViewModel(viewModel: MainScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavingsScreenViewModel::class)
    internal abstract fun bindSavingsScreenViewModel(viewModel: SavingsScreenViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)