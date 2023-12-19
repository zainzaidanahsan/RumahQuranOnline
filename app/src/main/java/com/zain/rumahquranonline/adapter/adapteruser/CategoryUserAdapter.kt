package com.zain.rumahquranonline.adapter.adapteruser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.data.DataCategory
import com.zain.rumahquranonline.databinding.ItemRowCategoryUserBinding
import com.zain.rumahquranonline.filter.FilterCategory
import com.zain.rumahquranonline.filter.FilterCategoryUser

class CategoryUserAdapter : RecyclerView.Adapter<CategoryUserAdapter.HolderCategory>, Filterable {
    private val context: android.content.Context
    public var categoryArrayList : ArrayList<DataCategory>
    private lateinit var binding : ItemRowCategoryUserBinding
    private var filterList : ArrayList<DataCategory>
    private var filter : FilterCategoryUser? = null

    constructor(context: android.content.Context, categoryArrayList: ArrayList<DataCategory>){
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }

    inner class HolderCategory(itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryTv =  binding.txtCategory
        val detail = binding.details
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = ItemRowCategoryUserBinding.inflate(LayoutInflater.from(context),parent, false)
        return HolderCategory(binding.root)
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category

        holder.categoryTv.text = category
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("categoryId", id)
            bundle.putString("category", category)
            it.findNavController().navigate(R.id.action_listCategoryUser_to_listPdfUser, bundle)

        }
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterCategoryUser(filterList, this)
        }
        return filter as FilterCategory
    }
}