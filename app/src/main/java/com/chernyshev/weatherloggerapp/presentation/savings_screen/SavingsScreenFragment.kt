package com.chernyshev.weatherloggerapp.presentation.savings_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.chernyshev.weatherloggerapp.databinding.FSavingsScreenBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SavingsScreenFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SavingsScreenViewModel
    private lateinit var viewBinding: FSavingsScreenBinding

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FSavingsScreenBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[SavingsScreenViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)
    }
}