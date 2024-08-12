package com.example.controlset.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.controlset.RecycleItem
import com.example.controlset.databinding.ActivityOtherBinding
import com.example.controlset.recycleChildClass.RecycleChildClassAdapter
import com.example.controlset.recycleChildView.RecycleChildViewAdapter

class OtherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtherBinding
    private val data1 = arrayListOf(
        RecycleItem("A"), RecycleItem("B"),
        RecycleItem("C"), RecycleItem("D"),
        RecycleItem("E"), RecycleItem("F")
    )
    private val data2 = arrayListOf(
        RecycleItem("G"), RecycleItem("H"),
        RecycleItem("I"), RecycleItem("J"),
        RecycleItem("K"), RecycleItem("L")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.recycleChildView.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recycleChildView.adapter = RecycleChildViewAdapter(data1)

        binding.recycleChildClass.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recycleChildClass.adapter = RecycleChildClassAdapter(data2)
        binding.recycleChildClass.setDeleteFunc {
            data2.removeAt(it)
            this.adapter!!.notifyItemRemoved(it)
            this.adapter!!.notifyItemRangeChanged(it, data2.size - it)
        }
    }
}