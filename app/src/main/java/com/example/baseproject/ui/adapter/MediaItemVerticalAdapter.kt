package com.example.baseproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.AdapterMediaItemVerticalBinding
import com.example.baseproject.model.MediaItemUI

class MediaItemVerticalAdapter (
    val context: Context,
    val onClickListener: (Int) -> Unit,
    val isTypeSelectButton: Boolean = false
): ListAdapter<MediaItemUI, MediaItemVerticalHolder>(MediaItemVerticalDiffUtil()) {

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemVerticalHolder {
        return MediaItemVerticalHolder(AdapterMediaItemVerticalBinding.inflate(layoutInflater,parent,false), onClickListener, isTypeSelectButton)
    }

    override fun onBindViewHolder(holder: MediaItemVerticalHolder, position: Int) {
        holder.bindData(getItem(position))
    }

}


class MediaItemVerticalHolder (
    val binding: AdapterMediaItemVerticalBinding,
    val onClickListener: (Int) -> Unit,
    val isTypeSelectButton: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onClickListener.invoke(bindingAdapterPosition)
        }
        if(isTypeSelectButton) {
            binding.imgSelectState.visibility = View.VISIBLE
        }
    }

    fun bindData(data: MediaItemUI) {
        binding.tvTitle.text = data.title
        binding.tvSubTitle.text = data.subTitle

        if(isTypeSelectButton) {
            if(data.isSelected){
                binding.imgSelectState.setImageResource(R.drawable.ic_done_24)
            }else {
                binding.imgSelectState.setImageResource(R.drawable.ic_add_24)
            }
        }

        Glide.with(binding.root)
            .load(data.iconUri)
            .placeholder(R.drawable.ic_default_art_24)
            .centerCrop()
            .into(binding.imgIcon)
    }
}

class MediaItemVerticalDiffUtil: DiffUtil.ItemCallback<MediaItemUI>() {
    override fun areItemsTheSame(oldItem: MediaItemUI, newItem: MediaItemUI): Boolean {
        return oldItem.id == newItem.id && oldItem.dataSource == newItem.dataSource
    }

    override fun areContentsTheSame(oldItem: MediaItemUI, newItem: MediaItemUI): Boolean {
        return oldItem.iconUri == newItem.iconUri && oldItem.title == newItem.title && oldItem.isSelected == newItem.isSelected
    }
}