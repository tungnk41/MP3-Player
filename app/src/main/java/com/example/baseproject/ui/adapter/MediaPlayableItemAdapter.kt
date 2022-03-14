package com.example.baseproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.databinding.AdapterMediaPlayableItemBinding
import com.example.baseproject.model.MediaItemUI

class MediaPlayableItemAdapter (
    val context: Context,
    val onClickListener: (Int) -> Unit,
): ListAdapter<MediaItemUI, MediaPlayableItemHolder>(MediaPlayableDiffUtil()) {

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPlayableItemHolder {
        return MediaPlayableItemHolder(AdapterMediaPlayableItemBinding.inflate(layoutInflater,parent,false), onClickListener)
    }

    override fun onBindViewHolder(holder: MediaPlayableItemHolder, position: Int) {
        holder.bindData(getItem(position))
    }

}


class MediaPlayableItemHolder (
    val binding: AdapterMediaPlayableItemBinding,
    val onClickListener: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onClickListener.invoke(bindingAdapterPosition)
        }
    }

    fun bindData(data: MediaItemUI) {
        binding.tvTitle.text = data.title
        binding.tvSubTitle.text = data.subTitle

        Glide.with(binding.root)
            .load(data.iconUri)
            .centerCrop()
            .into(binding.imgIcon)
    }
}

class MediaPlayableDiffUtil: DiffUtil.ItemCallback<MediaItemUI>() {
    override fun areItemsTheSame(oldItem: MediaItemUI, newItem: MediaItemUI): Boolean {
        return oldItem.id == newItem.id && oldItem.dataSource == newItem.dataSource
    }

    override fun areContentsTheSame(oldItem: MediaItemUI, newItem: MediaItemUI): Boolean {
        return oldItem.iconUri == newItem.iconUri && oldItem.title == newItem.title
    }

}