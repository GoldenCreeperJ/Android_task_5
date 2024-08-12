package com.example.controlset.recycleChildView

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controlset.RecycleItem
import com.example.controlset.databinding.RecycleChildViewItemBinding

class RecycleChildViewAdapter(private val data:ArrayList<RecycleItem>):RecyclerView.Adapter<RecycleChildViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RecycleChildViewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            RecycleChildViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.recycleChildView.setDeleteFunc {
            data.removeAt(position)
            this.notifyItemRemoved(position)
            this.notifyItemRangeChanged(position, data.size - position)
        }
        holder.binding.recycleChildViewText.text = data[position].string
    }

    override fun getItemCount(): Int = data.size
}