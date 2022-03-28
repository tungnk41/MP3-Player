package com.example.baseproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.AdapterPagerListSongBinding
import com.example.baseproject.model.PagerItem

class AddSongPagerAdapter(
     val context: Context,
     val onItemClickListener: (Int,Int) -> Unit,
) : ListAdapter<PagerItem, PagerHolder>(AddSongPagerDiffUtil()) {

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerHolder {
        return PagerHolder(AdapterPagerListSongBinding.inflate(layoutInflater,parent,false),onItemClickListener)
    }

    override fun onBindViewHolder(holder: PagerHolder, position: Int) {
        holder.bindData(getItem(position))
    }

}

class PagerHolder (
    val binding: AdapterPagerListSongBinding,
    val onItemClickListener: (Int,Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private val childAdapter = MediaItemVerticalAdapter(binding.root.context, onClickListener = {
        onItemClickListener.invoke(bindingAdapterPosition,it)
        }
    )

    init {
        binding.rvPagerListSong.adapter = childAdapter
        binding.rvPagerListSong.isNestedScrollingEnabled = false
    }

    fun bindData(data: PagerItem) {
        childAdapter.submitList(data.listItem)
    }
}

class AddSongPagerDiffUtil: DiffUtil.ItemCallback<PagerItem>() {
    override fun areItemsTheSame(oldItem: PagerItem, newItem: PagerItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PagerItem, newItem: PagerItem): Boolean {
        return oldItem == newItem
    }
}