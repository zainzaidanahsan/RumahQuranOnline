package com.zain.rumahquranonline.adapter.adapteradmin

import android.app.AlertDialog
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
import com.zain.rumahquranonline.constan.MyApplication
import com.zain.rumahquranonline.data.DataPdf
import com.zain.rumahquranonline.databinding.ItemListBukuAdminBinding
import com.zain.rumahquranonline.filter.FilterPdfAdmin

class ListBukuAdminAdapter: RecyclerView.Adapter<ListBukuAdminAdapter.HolderPdfAdmin>, Filterable {
    private var context : android.content.Context
    public var pdfArrayList : ArrayList<DataPdf>
    private var filterList : ArrayList<DataPdf>
    private lateinit var binding : ItemListBukuAdminBinding

    private var filter : FilterPdfAdmin? = null

    constructor(context: Context, pdfArrayList: ArrayList<DataPdf>):super(){
        this.context = context
        this.pdfArrayList = pdfArrayList
        this.filterList = pdfArrayList
    }
    inner class HolderPdfAdmin(itemView : View):RecyclerView.ViewHolder(itemView){

        val pdfView = binding.pdfView
        val judul = binding.judul
        val deskripsi = binding.descBook
        val category = binding.category
        val more = binding.buttonMore
        val progressBar = binding.pgBar
        val size = binding.txtSize
        val moreBtn = binding.buttonMore

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPdfAdmin {
        binding = ItemListBukuAdminBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderPdfAdmin(binding.root)
    }

    override fun getItemCount(): Int {
        return pdfArrayList.size
    }



    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterPdfAdmin(filterList, this)
        }
        return filter as FilterPdfAdmin
    }
    override fun onBindViewHolder(holder: HolderPdfAdmin, position: Int) {
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

        holder.moreBtn.setOnClickListener {
            moreOptionDialog(data, holder)
        }

    }

    private fun moreOptionDialog(data: DataPdf, holder: HolderPdfAdmin) {
        val bookId = data.id
        val bookUrl = data.url
        val bookTitle = data.title

        val options = arrayOf("Edit", "Delete")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Pilih Opsi")
            .setItems(options){dialog, positions ->
                if (positions == 0){
                    //edit
                    val bundle = Bundle()
                    bundle.putString("bookId", bookId)
                    holder.itemView.findNavController().navigate(R.id.action_listPdf_to_editPdf, bundle)

                }else if(positions == 1){
                    //delete
                    MyApplication.deleteBook(context, bookId, bookUrl, bookTitle)
                }
            }
            .show()
    }
}