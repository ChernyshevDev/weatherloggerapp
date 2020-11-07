package com.chernyshev.weatherloggerapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chernyshev.weatherloggerapp.databinding.VInformationItemBinding
import com.chernyshev.weatherloggerapp.domain.entity.Info
import javax.inject.Inject

class InfoListAdapter @Inject constructor() :
    RecyclerView.Adapter<InfoListAdapter.ItemViewHolder>() {

    private lateinit var items: List<Info>

    fun setItems(items: List<Info>) {
        this.items = items
    }

    inner class ItemViewHolder(private val binding: VInformationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViewData(item: Info) {
            with(binding) {
                vInformationTitle.text = item.description
                vInformationContent.text = item.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            VInformationItemBinding.inflate(
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