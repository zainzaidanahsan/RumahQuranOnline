package com.zain.rumahquranonline.ui.materi

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentEditPdfBinding
import com.zain.rumahquranonline.databinding.FragmentListPdfBinding

class EditPdf : Fragment() {
    private var _binding : FragmentEditPdfBinding?= null
    private val binding get() = _binding!!

    private var bookId = ""
    private lateinit var progressDialog: ProgressDialog
    private var categoryTitle  : ArrayList<String> = ArrayList()
    private var categoryId  : ArrayList<String> = ArrayList()

    private var selectedCategoryId = ""
    private var selectedCategoryTitle = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditPdfBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookId = arguments?.getString("bookId").toString()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Tunggu Sebentar")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.tvPilihkategori.setOnClickListener{
            categoriesDialog()
        }
        binding.buttonUploadMateri.setOnClickListener {
            validateData()
        }
        loadBookInfo()
        loadCCategory()



    }

    private fun loadBookInfo() {
        Log.d(TAG, "loadBookInfo: Memuat Info Buku")
        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    selectedCategoryId = snapshot.child("categoryID").value.toString()
                    val desc = snapshot.child("description").value.toString()
                    val judul = snapshot.child("title").value.toString()

                    binding.etJudul.setText(judul)
                    binding.etDesc.setText(desc)

                    Log.d(TAG, "onDataChange: loading kategori buku...")

                    val refBook = FirebaseDatabase.getInstance().getReference("Categories")
                    refBook.child(selectedCategoryId)
                        .addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val category = snapshot.child("category").value.toString()
                                binding.tvPilihkategori.text =  category
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }


    private var title = ""
    private var desc = ""
    private fun validateData() {
        title = binding.etJudul.text.toString().trim()
        desc = binding.etDesc.text.toString().trim()

        if (title.isEmpty() && desc.isEmpty()){
            Toast.makeText(requireContext(), "Jangan ada yang kosong!", Toast.LENGTH_SHORT).show()
        }else if (selectedCategoryId.isEmpty()){
            Toast.makeText(requireContext(), "Pilih Kategori!", Toast.LENGTH_SHORT).show()
        }else{
            update()
        }
    }

    private fun update() {
        Log.d(TAG, "update: Memulai Update Pdf..")

        progressDialog.setMessage("Memperbarui pdf..")
        progressDialog.show()
        val hashMap = HashMap<String, Any>()
        hashMap["title"] = "$title"
        hashMap["description"] = "$desc"
        hashMap["categoryID"] = "$selectedCategoryId"

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child(bookId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Log.d(TAG, "update: berhasil..")
                Toast.makeText(requireContext(), "Update Berhasil!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Log.d(TAG, "update: gagal update karena ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Gagal Update karena ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun categoriesDialog(){
        val categoriesArray = arrayOfNulls<String>(categoryTitle.size)
        for (z in categoryTitle.indices){
            categoriesArray[z] = categoryTitle[z]
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Kategori")
            .setItems(categoriesArray){dialog, positions->
                selectedCategoryId = categoryId[positions]
                selectedCategoryTitle = categoryTitle[positions]

                binding.tvPilihkategori.text = selectedCategoryTitle
            }
    }

    private fun loadCCategory() {
        Log.d(TAG, "loadCCategory: memuat kategori")
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryTitle.clear()
                categoryId.clear()
                for (z in snapshot.children){
                    val id  = ""+z.child("id").value
                    val category  =""+ z.child("category").value
                    categoryTitle.add(category)
                    categoryId.add(id)

                    Log.d(TAG, "onDataChange: Category ID $id")
                    Log.d(TAG, "onDataChange: Category $category")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


    private companion object{
        private const val TAG = "PDF_EDIT_TAG"
    }
}