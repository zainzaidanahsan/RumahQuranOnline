package com.zain.rumahquranonline.adapter.adapteradmin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.data.DataCategory
import com.zain.rumahquranonline.databinding.ItemRowCategoryBinding
import com.zain.rumahquranonline.filter.FilterCategory

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.HolderCategory>, Filterable {
    private val context: android.content.Context
    public var categoryArrayList : ArrayList<DataCategory>
    private lateinit var binding : ItemRowCategoryBinding
    private var filterList : ArrayList<DataCategory>
    private var filter : FilterCategory? = null

    constructor(context: android.content.Context, categoryArrayList: ArrayList<DataCategory>){
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }

    inner class HolderCategory(itemView:View): RecyclerView.ViewHolder(itemView){
        val categoryTv =  binding.txtCategory
        val deleteCat = binding.deleteCat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = ItemRowCategoryBinding.inflate(LayoutInflater.from(context),parent, false)
        return HolderCategory(binding.root)
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category
        val uid = model.uid
        val timestamp = model.timestamp

        holder.categoryTv.text = category
        holder.deleteCat.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Hapus")
                .setMessage("Hapus Category?")
                .setPositiveButton("Ya"){a, d->
                    Toast.makeText(context, "Menghapus..", Toast.LENGTH_SHORT).show()
                    deleteCategory(model, holder)
                }
                .setNegativeButton("Tidak"){a, d->
                    a.dismiss()
                }
                .show()
        }
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("categoryId", id)
            bundle.putString("category", category)
            it.findNavController().navigate(R.id.action_listBukuMateriAdmin_to_listPdf, bundle)
        }
    }

    private fun deleteCategory(data : DataCategory, holder: HolderCategory) {
        val id = data.id
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Berhasil Menghapus Category", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(context, "Gagal Menghapus Category ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterCategory(filterList, this)
        }
        return filter as FilterCategory
    }
}