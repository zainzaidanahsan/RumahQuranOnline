package com.zain.rumahquranonline.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.data.Materi

class MateriAdapter(private val listMateri: ArrayList<Materi>): RecyclerView.Adapter<MateriAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var judul: TextView = itemView.findViewById(R.id.tv_item_name)
        var icon:ImageView = itemView.findViewById(R.id.img_icon)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}