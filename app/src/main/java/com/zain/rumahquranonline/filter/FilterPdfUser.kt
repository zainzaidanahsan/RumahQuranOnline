package com.zain.rumahquranonline.filter

import com.zain.rumahquranonline.adapter.adapteruser.ListPdfUserAdapter
import com.zain.rumahquranonline.data.DataPdf

class FilterPdfUser: android.widget.Filter {
    var filterList : ArrayList<DataPdf>
    var adapterPdfUser : ListPdfUserAdapter


    constructor(filterList: ArrayList<DataPdf>, adapterPdfAdmin: ListPdfUserAdapter){
        this.filterList = filterList
        this.adapterPdfUser = adapterPdfAdmin
    }
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint : CharSequence? = constraint
        val result = FilterResults()
        if (constraint != null && constraint.isNotEmpty()){
            constraint = constraint.toString().lowercase()

            var filteredModels = ArrayList<DataPdf>()
            for (i in filterList.indices){
                if ( filterList[i].title.lowercase().contains(constraint)){
                    filteredModels.add(filterList[i])
                }
            }
            result.count= filteredModels.size
            result.values = filteredModels

        }else{
            result.count= filterList.size
            result.values = filterList
        }
        return result
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterPdfUser.pdfArrayList = results.values as ArrayList<DataPdf>
        adapterPdfUser.notifyDataSetChanged()
    }
}