package com.example.baseproject.ui.tabLocalMusic.adapter

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.databinding.AdapterMediaItemBinding
import com.example.baseproject.model.MediaItemExtra

class MediaItemAdapter (
    val context: Context,
    val onClickListener: (Int,Boolean) -> Unit,
): ListAdapter<MediaItemExtra, MediaItemHolder>(MediaItemDiffUtil()) {

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemHolder {
        return MediaItemHolder(AdapterMediaItemBinding.inflate(layoutInflater,parent,false), onClickListener)
    }

    override fun onBindViewHolder(holder: MediaItemHolder, position: Int) {
        holder.bindData(getItem(position))
    }

}


class MediaItemHolder (
    val binding: AdapterMediaItemBinding,
    val onClickListener: (Int,Boolean) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {

    }

    fun bindData(data: MediaItemExtra) {
        binding.tvTitle.setText(data.title)
    }
}

class MediaItemDiffUtil: DiffUtil.ItemCallback<MediaItemExtra>() {
    override fun areItemsTheSame(oldItem: MediaItemExtra, newItem: MediaItemExtra): Boolean {
        return oldItem.id == newItem.id && oldItem.dataSource == newItem.dataSource
    }

    override fun areContentsTheSame(oldItem: MediaItemExtra, newItem: MediaItemExtra): Boolean {
        return oldItem.iconUri == newItem.iconUri && oldItem.title == newItem.title
    }


}