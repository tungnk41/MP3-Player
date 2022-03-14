package com.example.baseproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.AdapterMediaBrowsableItemBinding
import com.example.baseproject.model.MediaItemUI

class MediaBrowsableItemAdapter (
    val context: Context,
    val onClickListener: (Int) -> Unit,
): ListAdapter<MediaItemUI, MediaBrowsableItemHolder>(MediaBrowsableItemDiffUtil()) {

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaBrowsableItemHolder {
        return MediaBrowsableItemHolder(AdapterMediaBrowsableItemBinding.inflate(layoutInflater,parent,false), onClickListener)
    }

    override fun onBindViewHolder(holder: MediaBrowsableItemHolder, position: Int) {
        holder.bindData(getItem(position))
    }

}


class MediaBrowsableItemHolder (
    val binding: AdapterMediaBrowsableItemBinding,
    val onClickListener: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onClickListener.invoke(bindingAdapterPosition)
        }
    }

    fun bindData(data: MediaItemUI) {
        binding.tvTitle.text = data.title
    }
}

class MediaBrowsableItemDiffUtil: DiffUtil.ItemCallback<MediaItemUI>() {
    override fun areItemsTheSame(oldItem: MediaItemUI, newItem: MediaItemUI): Boolean {
        return oldItem.id == newItem.id && oldItem.dataSource == newItem.dataSource
    }

    override fun areContentsTheSame(oldItem: MediaItemUI, newItem: MediaItemUI): Boolean {
        return oldItem.iconUri == newItem.iconUri && oldItem.title == newItem.title
    }

}