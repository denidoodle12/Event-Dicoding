package com.example.mysubmissionandro.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysubmissionandro.data.remote.response.ListEventsItem
import com.example.mysubmissionandro.databinding.RowItemsBinding

class EventAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val events = getItem(position)
        holder.bind(events, onItemClick)
    }

    class MyViewHolder(private val binding: RowItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(eventsItem: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            Glide.with(binding.ivMediaCover.context)
                .load(eventsItem.mediaCover)
                .into(binding.ivMediaCover)
            binding.tvEventName.text = eventsItem.name

            binding.root.setOnClickListener {
                onItemClick(eventsItem)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListEventsItem> =
            object : DiffUtil.ItemCallback<ListEventsItem>() {
                override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                    return oldItem == newItem
                }
            }
    }

}
