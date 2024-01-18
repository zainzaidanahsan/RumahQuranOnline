package com.zain.rumahquranonline.ui.Profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.contentValuesOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentEditProfileBinding
import java.net.URI


class EditProfile : Fragment() {
    private var _binding : FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    private var imageUri : Uri? = null
    private lateinit var progressDialog : ProgressDialog


//    private val CAMERA_REQUEST_CODE = 100
//    private val GALLERY_REQUEST_CODE = 101
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Tunggu Sebentar")
        progressDialog.setCanceledOnTouchOutside(false)

        loadUserInfo()
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonSubmit.setOnClickListener{
            validateData()
        }

        binding.buttonEdit.setOnClickListener {
            setupPopUp()
        }


    }

    private var username = ""
    private fun validateData() {
        username = binding.etUsername.text.toString().trim()
        if (username.isEmpty()){
            Toast.makeText(requireContext(), "Masukkan Nama!", Toast.LENGTH_SHORT).show()
        }
        else{
            if (imageUri == null){
                updateProfile("")
            }
            else{
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Mengupload Foto")
        progressDialog.show()
        val filePathName = "PhotoImage/"+auth.uid
        val ref = FirebaseStorage.getInstance().getReference(filePathName)
        ref.putFile(imageUri!!)
            .addOnSuccessListener {
                val uriTask :Task<Uri> = it.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedImgUrl = "${uriTask.result}"
                updateProfile(uploadedImgUrl)
                Toast.makeText(requireContext(), "Berhasil Upload", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Gagal Upload karena ${it.message}!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(uploadImgUrl: String) {
        progressDialog.setMessage("Update Profile")
        val hashMap : HashMap<String, Any> = HashMap()
        hashMap["username"] = "$username"
        if (imageUri != null){
            hashMap["photoImage"] = uploadImgUrl
        }

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()

                Toast.makeText(requireContext(), "Berhasil Update Profile", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Gagal Update karena ${it.message}!", Toast.LENGTH_SHORT).show()
            }

    }

    private fun setupPopUp() {
        val popUp = PopupMenu(requireContext(), binding.buttonEdit)
        popUp.menu.add(Menu.NONE, 0, 0 ,"Camera")
        popUp.menu.add(Menu.NONE, 1, 1 , "Gallery")
        popUp.setOnMenuItemClickListener {
            val id = it.itemId
            if (id == 0){
                bukaKamera()
            }else if (id==1){
                bukaGallery()
            }
            true
        }
        popUp.show()

    }

    private fun bukaGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
//        startActivityForResult(intent, GALLERY_REQUEST_CODE)
        galleryActivityResultLauncher.launch(intent)
    }

    private fun bukaKamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Tittle")
        values.put(MediaStore.Images.Media.TITLE, "Temp_Description")
        val imageUri: Uri? = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    private fun loadUserInfo(){
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val username = "${snapshot.child("username").value}"
                    val imageProfile = "${snapshot.child("photoImage").value}"

                    binding.etUsername.setText(username)
                    try {
                        Glide.with(requireActivity())
                            .load(imageProfile)
                            .placeholder(R.drawable.baseline_person_24)
                            .into(binding.circleImageView)
                    }catch (e:Exception){

                    }


                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {
            if (it.resultCode == Activity.RESULT_OK){
                val data = it.data
                imageUri = data?.data
            }else{
                Toast.makeText(requireContext(),"Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )
    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {
            if (it.resultCode == Activity.RESULT_OK){
                val data = it.data
                imageUri = data?.data
                binding.circleImageView.setImageURI(imageUri)
            }else{
                Toast.makeText(requireContext(),"Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )


}