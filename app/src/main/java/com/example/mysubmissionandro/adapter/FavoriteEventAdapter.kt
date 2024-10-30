package com.example.mysubmissionandro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysubmissionandro.data.local.entity.EventEntity
import com.example.mysubmissionandro.databinding.RowItemsBinding

class FavoriteEventAdapter(private val onItemClick: (EventEntity) -> Unit) :
    ListAdapter<EventEntity, FavoriteEventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val favoriteEvent = getItem(position)
        holder.bind(favoriteEvent, onItemClick)
    }

    class MyViewHolder(private val binding: RowItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEvent: EventEntity, onItemClick: (EventEntity) -> Unit) {
            Glide.with(binding.ivMediaCover.context)
                .load(favoriteEvent.mediaCover)
                .into(binding.ivMediaCover)
            binding.tvEventName.text = favoriteEvent.name

            binding.root.setOnClickListener {
                onItemClick(favoriteEvent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
