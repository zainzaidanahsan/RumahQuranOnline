package com.zain.rumahquranonline.ui.materi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zain.rumahquranonline.adapter.adapteradmin.ListBukuAdminAdapter
import com.zain.rumahquranonline.data.DataPdf
import com.zain.rumahquranonline.databinding.FragmentListPdfBinding


class ListPdf : Fragment() {
    private var _binding : FragmentListPdfBinding?= null
    private val binding get() = _binding!!

    private var categoryId = ""
    private var category = ""
    private lateinit var pdfArrayList:ArrayList<DataPdf>
    private lateinit var adapter: ListBukuAdminAdapter

    companion object{
        const val TAG = "PDF_LIST_ADMIN_TAG"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListPdfBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId = arguments?.getString("categoryId")!!
        category = arguments?.getString("category")!!
        binding.rvListPdf.layoutManager = LinearLayoutManager(requireContext())

        loadListPdf()
        binding.editTextSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapter.filter!!.filter(p0)
                }catch (e:Exception){
                    Log.d(TAG, "onTextChanged: ${e.message}")
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun loadListPdf() {
        pdfArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.orderByChild("categoryID").equalTo(categoryId)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    pdfArrayList.clear()
                    if (isAdded){
                        for (ds in snapshot.children){
                            val model = ds.getValue(DataPdf::class.java)
                            if (model != null){
                                pdfArrayList.add(model)
                                Log.d(TAG, "onDataChange: ${model.title} ${model.categoryID}")
                            }
                        }
                    }
                    if(isAdded){
                        adapter = ListBukuAdminAdapter(requireContext(),pdfArrayList)
                        binding.rvListPdf.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }


}