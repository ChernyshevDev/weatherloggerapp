package com.example.weatherloggerapp.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface ViewStateHolder<T> {
    fun updateState(stateCopy: (T?) -> T)
    val viewState: LiveData<T>
}

class ViewStateHolderImpl<T> :
    ViewStateHolder<T> {
    private val _viewState = MutableLiveData<T>()

    override fun updateState(stateCopy: (T?) -> T) { // TODO WHAT DOES IT MEAN
        val oldState = _viewState.value
        _viewState.postValue(stateCopy(oldState))
    }

    override val viewState: LiveData<T>
        get() = _viewState

}

fun <T> Fragment.onChangeState(viewModel: ViewStateHolder<T>, onUpdate: (T) -> Unit) {
    viewModel.viewState.observe(viewLifecycleOwner, Observer {
        onUpdate(it)
    })
}