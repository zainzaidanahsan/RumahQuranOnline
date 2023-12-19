package com.zain.rumahquranonline.ui.materiAdmin

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentMateriAdminBinding

class MateriAdmin : Fragment() {

    private var _binding : FragmentMateriAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMateriAdminBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Tunggu sebentar")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.buttonTambahKategori.setOnClickListener {
            validateData()
        }

    }

    private var category = ""
    private fun validateData() {
        category = binding.etSubkategori.text.toString()
        if (category.isEmpty()){
            Toast.makeText(requireContext(), "Isi Kategori", Toast.LENGTH_SHORT).show()
        }else{
            addCategoryFirebase()
        }
    }

    private fun addCategoryFirebase() {
        progressDialog.show()
        val timestamp = System.currentTimeMillis()

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["category"] = category
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "${firebaseAuth.uid}"


        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child("$timestamp").setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Kategori berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Kategori gagal ditambahkan ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}