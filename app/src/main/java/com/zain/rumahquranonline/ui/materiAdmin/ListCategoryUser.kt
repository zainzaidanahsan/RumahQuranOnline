package com.zain.rumahquranonline.ui.materiAdmin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zain.rumahquranonline.adapter.adapteruser.CategoryUserAdapter
import com.zain.rumahquranonline.data.DataCategory
import com.zain.rumahquranonline.databinding.FragmentListCategoryUserBinding

class ListCategoryUser : Fragment() {
    private var _binding : FragmentListCategoryUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var categoryArrayList: ArrayList<DataCategory>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryUserAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListCategoryUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        loadCategory()
        binding.editTextSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    adapter.filter.filter(p0)
                }
                catch (e: Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }
    private fun loadCategory() {
        categoryArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(DataCategory::class.java)
                    categoryArrayList.add((model!!))
                }
                if (isAdded) {
                    recyclerView = binding.rvListbuku
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    adapter = CategoryUserAdapter(requireContext(), categoryArrayList)
                    binding.rvListbuku.adapter = adapter
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}