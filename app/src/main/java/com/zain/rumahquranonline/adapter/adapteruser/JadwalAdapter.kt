package com.zain.rumahquranonline.adapter.adapteruser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.data.Jadwal

class JadwalAdapter(private val jadwalList: List<Jadwal>): RecyclerView.Adapter<JadwalAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTanggal: TextView = view.findViewById(R.id.txtTanggal)
        val textViewSesi: TextView = view.findViewById(R.id.txtSesi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.jadwal_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jadwalList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jadwal = jadwalList[position]
        holder.textViewTanggal.text = jadwal.tanggal
        holder.textViewSesi.text = jadwal.sesi    }
}