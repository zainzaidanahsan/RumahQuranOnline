package com.zain.rumahquranonline.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zain.rumahquranonline.R


import android.view.LayoutInflater
import com.zain.rumahquranonline.model.HadistResponseItem

class HadistAdapter (private val itemClickListener: (HadistResponseItem) -> Unit) : RecyclerView.Adapter<HadistAdapter.HadisViewHolder>() {

    private var hadisList: List<HadistResponseItem> = ArrayList()

    fun setData(newHadisList: List<HadistResponseItem>) {
        hadisList = newHadisList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HadisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hadist, parent, false)
        return HadisViewHolder(view)
    }

    override fun onBindViewHolder(holder: HadisViewHolder, position: Int) {
        val currentHadis = hadisList[position]

        holder.titleTextView.text = currentHadis.name
        holder.contentTextView.text = currentHadis.total.toString()

        holder.itemView.setOnClickListener {
            itemClickListener.invoke(currentHadis)
        }
    }

    override fun getItemCount(): Int {
        return hadisList.size
    }

    class HadisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
    }
}
