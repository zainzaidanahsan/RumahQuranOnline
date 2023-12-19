package com.zain.rumahquranonline.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.data.NumberHadistItem
import com.zain.rumahquranonline.model.DetailHadistResponse
import com.zain.rumahquranonline.ui.materi.DetailHadist

class NomorHadistAdapter(private val nomorHadistList: List<Int>,
                         private val hadistList: List<DetailHadistResponse>) :
    RecyclerView.Adapter<NomorHadistAdapter.NomorHadistViewHolder>()  {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NomorHadistAdapter.NomorHadistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nomor_hadist, parent, false)
        return NomorHadistViewHolder(view)
    }

    override fun onBindViewHolder(holder: NomorHadistAdapter.NomorHadistViewHolder, position: Int) {
        val currentNomorHadist = nomorHadistList[position]
        holder.nomorTextView.text = currentNomorHadist.toString()
        holder.itemView.setOnClickListener{
            val selectedHadist = hadistList[position]
            val bundle = Bundle().apply {
                putInt("number", selectedHadist.number)
                putString("arab", selectedHadist.arab)
                putString("id", selectedHadist.id)
        }
        it.findNavController().navigate(R.id.action_nomorhadist_to_detailHadist, bundle)
        }
    }

    override fun getItemCount(): Int {
        return nomorHadistList.size
    }

    class NomorHadistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomorTextView: TextView = itemView.findViewById(R.id.number_hadits)
    }
}