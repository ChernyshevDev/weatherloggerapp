package com.chernyshev.weatherloggerapp.presentation.more_info_dialog

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.chernyshev.weatherloggerapp.databinding.DMoreInfoBinding
import com.chernyshev.weatherloggerapp.domain.entity.Info
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

private lateinit var viewBinding: DMoreInfoBinding

class MoreInfoDialog  : DialogFragment() {

    @Inject
    lateinit var adapter: MoreInfoAdapter

    companion object{
        const val TEMPERATURE = "temperature"
        const val CITY = "city"
        const val DATE = "date"
        const val TIME = "time"
        const val DESCRIPTION = "description"
        const val PRESSURE = "pressure"
        const val WIND_SPEED = "wind_speed"
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DMoreInfoBinding.inflate(inflater)
        isCancelable = true
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.moreInfoRecycler.adapter = adapter

        val bundle = this.arguments
        bundle?.let{bundle ->

            with(viewBinding){
                moreInfoWeatherDate.text = bundle.getString(DATE)
                moreInfoWeatherTime.text = bundle.getString(TIME)
            }

            val listOfItems = getItemsFromBundle(bundle)
            adapter.setItems(listOfItems)

        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun getItemsFromBundle(bundle: Bundle) : List<Info>{
        return listOf(
            Info( description = "Temperature", content = bundle.getString(TEMPERATURE)!!),
            Info( description = "Description", content = bundle.getString(DESCRIPTION)!!),
            Info( description = "Wind Speed", content = bundle.getString(WIND_SPEED)!!),
            Info( description = "Pressure", content = bundle.getString(PRESSURE)!!),
            Info( description = "City", content = bundle.getString(CITY)!!)
        )
    }
}