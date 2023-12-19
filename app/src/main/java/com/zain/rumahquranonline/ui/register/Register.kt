package com.zain.rumahquranonline.ui.register

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentLoginFirebaseBinding
import com.zain.rumahquranonline.databinding.FragmentRegisterBinding
import java.util.regex.Pattern

class Register : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()


        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Tunggu Sebentar..")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.buttonRegister.setOnClickListener {
            validateData()
        }


    }
    private var username = ""
    private var email = ""
    private var password =""

    private fun validateData(){
        username = binding.editTextUsername.text.toString().trim()
        email =  binding.editTextEmail.text.toString().trim()
        password = binding.editTextPassword.text.toString().trim()
        val cPass = binding.editTextConfirmPassword.text.toString().trim()


        if (username.isEmpty()){
            Toast.makeText(requireContext(),"Username tidak boleh kosong.", Toast.LENGTH_SHORT).show()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(requireContext(),"Format Email Salah.", Toast.LENGTH_SHORT).show()
        }else if (password.isEmpty()){
            Toast.makeText(requireContext(),"Password tidak boleh kosong.", Toast.LENGTH_SHORT).show()
        }else if (password != cPass){
            Toast.makeText(requireContext(),"Password tidak sama!.", Toast.LENGTH_SHORT).show()
        }else{
            createUserAccount()
        }


    }

    private fun createUserAccount(){
        progressDialog.setMessage("Membuat Akun...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserInfo()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Pembuatan Akun gagal karena ${e.message}.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo(){
        progressDialog.setMessage("Menyimpan akun...")

        val timestamp = System.currentTimeMillis()
        val uid = firebaseAuth.uid
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = uid!!
        hashMap["email"] = email
        hashMap["username"] = username
        hashMap["photoImage"] = ""
        hashMap["userType"] = "user"

        hashMap["timeStamp"] = timestamp
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Akun berhasil terbuat", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_register_to_login)
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Pembuatan Akun gagal ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}