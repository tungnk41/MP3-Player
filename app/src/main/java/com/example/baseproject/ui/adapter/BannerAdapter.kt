package com.example.baseproject.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.AdapterBannerBinding
import com.example.baseproject.databinding.AdapterMediaItemHorizontalBinding
import com.example.baseproject.model.Banner
import com.example.baseproject.model.MediaItemUI

class BannerAdapter(val context: Context,): ListAdapter<Banner,BannerViewHolder>(BannerDiffUtil()) {
    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(AdapterBannerBinding.inflate(layoutInflater,parent,false))
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

}

class BannerViewHolder(val binding: AdapterBannerBinding): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {

        }
    }

    fun bindData(banner: Banner) {
        Glide.with(binding.root)
            .load(banner.imageUrl)
            .placeholder(R.drawable.ic_default_art_24)
            .centerCrop()
            .into(binding.imgBannerItem)
    }
}

class BannerDiffUtil: DiffUtil.ItemCallback<Banner>() {
    override fun areItemsTheSame(oldItem: Banner, newItem: Banner): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Banner, newItem: Banner): Boolean {
        return oldItem.imageUrl.equals(newItem.imageUrl)
    }

}


