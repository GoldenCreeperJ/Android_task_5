package com.example.controlset.recycleChildClass

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.controlset.RecycleItem
import com.example.controlset.databinding.RecycleChildClassItemBinding

class RecycleChildClassAdapter(private val data:ArrayList<RecycleItem>):RecyclerView.Adapter<RecycleChildClassAdapter.ViewHolder>() {
    inner class ViewHolder( val binding: RecycleChildClassItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(RecycleChildClassItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.recycleChildViewText.text=data[position].string }

    override fun getItemCount(): Int =data.size
}