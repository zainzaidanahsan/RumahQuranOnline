package com.zain.rumahquranonline.ui.materiAdmin

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.data.DataCategory
import com.zain.rumahquranonline.databinding.FragmentHomeAdminBinding
import com.zain.rumahquranonline.databinding.FragmentUploadMateriBinding
import java.sql.Timestamp
import kotlin.math.log


class UploadMateri : Fragment() {
    private var _binding: FragmentUploadMateriBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryArratList: ArrayList<DataCategory>
    private var pdfUri : Uri? =null

    private val TAG =  "PDF_ADD_TAG"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentUploadMateriBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        loadPdfCategories()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Tunggu")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.tvPilihkategori.setOnClickListener{
            categoryDialog()
        }

        binding.btnFile.setOnClickListener {
            pdfIntent()
        }


        binding.buttonUploadMateri.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        Log.d(TAG, "Validate data: Validating data")

        title = binding.etJudul.text.toString().trim()
        deskripsi = binding.etDesc.text.toString().trim()
        category = binding.tvPilihkategori.text.toString().trim()
        if (title.isEmpty() || deskripsi.isEmpty() || category.isEmpty()){
            Toast.makeText(requireContext(), "Tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }else if (pdfUri == null){
            Toast.makeText(requireContext(), "Tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadPdftoStorage()
        }


    }

    private fun uploadPdftoStorage() {
        Log.d(TAG, "uploadPdftoStorage: uploading to storage...")
        progressDialog.setMessage("Mengupload pdf...")
        progressDialog.show()

        val timestamp = System.currentTimeMillis()

        val filePathAndName  = "Books/$timestamp"
        val storageReference = FirebaseStorage.getInstance().getReference(filePathAndName)
        storageReference.putFile(pdfUri!!)
            .addOnSuccessListener {taskSnapshot->
                Log.d(TAG, "uploadPdftoStorage: pdf uploaded now getting url..")

                val uriTask : Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedUrl = "${uriTask.result}"
                uploadPdfInfoToDn(uploadedUrl, timestamp)
            }
            .addOnFailureListener {e->
                Log.d(TAG, "uploadPdftoStorage: gagal upload pdf ke storage karena ${e.message} ")
                Toast.makeText(requireContext(), "Gagal mengupload ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun uploadPdfInfoToDn(uploadedPdfUrl:String, timestamp: Long) {
        Log.d(TAG, "uploadPdfInfoToDn: uploading to db")
        progressDialog.setMessage("Mengupload PDF..")

        val uid = firebaseAuth.uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = "$uid"
        hashMap["id"] = "$timestamp"
        hashMap["title"] = "$title"
        hashMap["description"] = "$deskripsi"
        hashMap["categoryID"] = "$selectedCategoryId"
        hashMap["url"] = "$uploadedPdfUrl"
        hashMap["timestamp"] = timestamp
        hashMap["viewsCount"] = 0
        hashMap["downloadsCount"] = 0

        val ref = FirebaseDatabase.getInstance().getReference("Books")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "uploadPdfInfoToDn: upload to db")
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Berhasil Upload", Toast.LENGTH_SHORT).show()
                pdfUri = null
            }
            .addOnFailureListener{e->
                Log.d(TAG, "uploadPdfInfoToDn: gagal upload karena ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Gagal mengupload ${e.message}", Toast.LENGTH_SHORT).show()
            }


    }

    private var title = ""
    private var deskripsi =""
    private var category = ""
    private var selectedCategoryId = ""
    private var selectedCategoryTitle = ""
    private fun loadPdfCategories() {
        Log.d(TAG, "loadPdfCategories: Loading pdf Categories")
        categoryArratList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArratList.clear()
                for (ds in snapshot.children){
                    val model = ds.getValue(DataCategory::class.java)
                    categoryArratList.add(model!!)
                    Log.d(TAG, "OnDataChange: ${model.category}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun categoryDialog(){
        Log.d(TAG, "categoryDialog : Showing pdf category pick dialog")
        val categoriesArray = arrayOfNulls<String>(categoryArratList.size)
        for (i in categoryArratList.indices){
            categoriesArray[i] =categoryArratList[i].category
        }


        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Kategori")
            .setItems(categoriesArray){dialog, which ->
                selectedCategoryId = categoryArratList[which].id
                selectedCategoryTitle = categoryArratList[which].category
                binding.tvPilihkategori.text = selectedCategoryTitle
                Log.d(TAG, "categoryDialog: Selected category ID $selectedCategoryId")
                Log.d(TAG, "categoryDialog: Selected category Title $selectedCategoryTitle")
            }
            .show()
    }

    private fun pdfIntent(){
        Log.d(TAG, "pdfIntent: Starting pick pdf intent")

        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        pdfresultLauncher.launch(intent)

    }
    val pdfresultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {result ->
            if (result.resultCode == RESULT_OK){
                Log.d(TAG, "pdf picked")
                pdfUri = result.data!!.data
            }else{
                Log.d(TAG, "PDF pick Cancelled")
                Toast.makeText(requireContext(), "Batal", Toast.LENGTH_SHORT).show()
            }
        }
    )

}