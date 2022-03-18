package com.example.baseproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.AdapterMediaItemVerticalBinding
import com.example.baseproject.databinding.AdapterMediaPlaylistBinding
import com.example.baseproject.model.MediaItemUI

class PlaylistAdapter (
    val context: Context,
    val onClickListener: (Int) -> Unit,
): ListAdapter<MediaItemUI, PlaylistHolder>(PlaylistDiffUtil()) {

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
        return PlaylistHolder(AdapterMediaPlaylistBinding.inflate(layoutInflater,parent,false), onClickListener)
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.bindData(getItem(position))
    }

}


class PlaylistHolder (
    val binding: AdapterMediaPlaylistBinding,
    val onClickListener: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onClickListener.invoke(bindingAdapterPosition)
        }
    }

    fun bindData(data: MediaItemUI) {
        binding.tvTitle.text = data.title

        Glide.with(binding.root)
            .load(data.iconUri)
            .placeholder(R.drawable.ic_default_art_24)
            .centerCrop()
            .into(binding.imgIcon)
    }
}

class PlaylistDiffUtil: DiffUtil.ItemCallback<MediaItemUI>() {
    override fun areItemsTheSame(oldItem: MediaItemUI, newItem: MediaItemUI): Boolean {
        return oldItem.id == newItem.id && oldItem.dataSource == newItem.dataSource
    }

    override fun areContentsTheSame(oldItem: MediaItemUI, newItem: MediaItemUI): Boolean {
        return oldItem.iconUri == newItem.iconUri && oldItem.title == newItem.title
    }
}