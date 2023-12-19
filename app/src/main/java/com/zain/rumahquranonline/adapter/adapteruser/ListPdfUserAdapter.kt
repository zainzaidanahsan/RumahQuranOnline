package com.zain.rumahquranonline.adapter.adapteruser

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.adapter.adapteradmin.ListBukuAdminAdapter
import com.zain.rumahquranonline.constan.MyApplication
import com.zain.rumahquranonline.data.DataPdf
import com.zain.rumahquranonline.databinding.ItemListBukuAdminBinding
import com.zain.rumahquranonline.filter.FilterPdfUser

class ListPdfUserAdapter: RecyclerView.Adapter<ListPdfUserAdapter.HolderPdfUser>, Filterable {
    private var context : android.content.Context
    public var pdfArrayList : ArrayList<DataPdf>
    private var filterList : ArrayList<DataPdf>
    private lateinit var binding : ItemListBukuAdminBinding

    private var filter : FilterPdfUser? = null

    constructor(context: Context, pdfArrayList: ArrayList<DataPdf>):super(){
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filterList = pdfArrayList
    }

    inner class HolderPdfUser(itemView : View): RecyclerView.ViewHolder(itemView){

        val pdfView = binding.pdfView
        val judul = binding.judul
        val deskripsi = binding.descBook
        val category = binding.category
        val more = binding.buttonMore
        val progressBar = binding.pgBar
        val size = binding.txtSize
        val moreBtn = binding.buttonMore

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListPdfUserAdapter.HolderPdfUser {
        binding = ItemListBukuAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderPdfUser(binding.root)
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }

    override fun onBindViewHolder(holder: ListPdfUserAdapter.HolderPdfUser, position: Int) {
        val data = pdfArrayList[position]
        val pdfId = data.id
        val categoryId= data.categoryID
        val title= data.title
        val description = data.description
        val pdfUrl = data.url
        val timestamp = data.timestamp

        holder.judul.text = title
        holder.deskripsi.text = description
        MyApplication.loadCategory(categoryId, holder.category)
        MyApplication.loadPdfFromUrlSinglePage(pdfUrl, title, holder.pdfView, holder.progressBar, null)
        MyApplication.loadPdfSize(pdfUrl, title, holder.size)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("bookId", pdfId)
            it.findNavController().navigate(R.id.action_listPdfUser_to_bacaMateri, bundle)
        }
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterPdfUser(filterList, this)
        }
        return filter as FilterPdfUser
    }


}