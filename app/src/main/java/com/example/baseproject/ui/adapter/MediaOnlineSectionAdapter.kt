package com.example.baseproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.AdapterMediaItemVerticalBinding
import com.example.baseproject.databinding.AdapterMediaOnlineBinding
import com.example.baseproject.model.MediaItemUI
import com.example.baseproject.model.MediaOnlineItem

class MediaOnlineSectionAdapter(
    val context: Context,
    val onExpandedClickListener: (Int) -> Unit,
    val onItemClickListener: (Int,Int) -> Unit,
    ) : ListAdapter<MediaOnlineItem,MediaOnlineSectionHolder>(MediaOnlineSectionDiffUtil()) {

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaOnlineSectionHolder {
        return return MediaOnlineSectionHolder(AdapterMediaOnlineBinding.inflate(layoutInflater,parent,false),onExpandedClickListener,onItemClickListener)
    }

    override fun onBindViewHolder(holder: MediaOnlineSectionHolder, position: Int) {
        holder.bindData(getItem(position))
    }
}

class MediaOnlineSectionHolder (
    val binding: AdapterMediaOnlineBinding,
    val onExpandedClickListener: (Int) -> Unit,
    val onItemClickListener: (Int,Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private val childAdapter = MediaItemHorizontalAdapter(binding.root.context, onClickListener = {
        onItemClickListener.invoke(bindingAdapterPosition,it)
    })

    init {
        binding.rvListChildMediaItem.adapter = childAdapter
        binding.rvListChildMediaItem.isNestedScrollingEnabled = false
        binding.combTitle.setOnClickListener {
            onExpandedClickListener.invoke(bindingAdapterPosition)
        }
    }

    fun bindData(data: MediaOnlineItem) {
        binding.tvTitle.text = data.title
        childAdapter.submitList(data.listChild)
    }
}

class MediaOnlineSectionDiffUtil: DiffUtil.ItemCallback<MediaOnlineItem>() {
    override fun areItemsTheSame(oldItem: MediaOnlineItem, newItem: MediaOnlineItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MediaOnlineItem, newItem: MediaOnlineItem): Boolean {
        return oldItem.title.equals(newItem.title)
    }
}