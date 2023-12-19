package com.zain.rumahquranonline.filter

import android.widget.Filter
import com.zain.rumahquranonline.adapter.adapteruser.CategoryUserAdapter
import com.zain.rumahquranonline.data.DataCategory

class FilterCategoryUser: Filter {
    private var filterList : ArrayList<DataCategory>

    private var adapterCategory : CategoryUserAdapter

    constructor(filterList : ArrayList<DataCategory>, adapterCategory: CategoryUserAdapter){
        this.filterList = filterList
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(p0: CharSequence?): FilterResults {
        var constraint = p0
        val result = FilterResults()
        if (constraint != null && constraint.isNotEmpty()){
            constraint = constraint.toString().uppercase()
            val filteredData : ArrayList<DataCategory> = ArrayList()

            for(i in 0 until filterList.size){
                if (filterList[i].category.uppercase().contains(constraint)){
                    filteredData.add(filterList[i])
                }
            }
            result.count = filteredData.size
            result.values = filteredData
        }else{
            result.count = filterList.size
            result.values = filterList
        }
        return result
    }

    override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
        adapterCategory.categoryArrayList = p1?.values as ArrayList<DataCategory>
        adapterCategory.notifyDataSetChanged()
    }
}