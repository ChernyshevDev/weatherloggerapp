package com.chernyshev.weatherloggerapp.presentation.more_info_dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chernyshev.weatherloggerapp.databinding.VInformationBinding
import com.chernyshev.weatherloggerapp.domain.entity.Info
import javax.inject.Inject

class MoreInfoAdapter @Inject constructor() :
RecyclerView.Adapter<MoreInfoAdapter.ItemViewHolder>() {

    private lateinit var items: List<Info>

    fun setItems(items: List<Info>){
        this.items = items
    }

    inner class ItemViewHolder(private val binding: VInformationBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bindViewData(item: Info){
            with(binding){
                vInformationTitle.text = item.description
                vInformationContent.text = item.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            VInformationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindViewData(items[position])
    }

    override fun getItemCount(): Int = items.size
}