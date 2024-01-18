package com.zain.rumahquranonline.ui.login

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentCreateScheduleBinding
import com.zain.rumahquranonline.databinding.FragmentLupaPasswordBinding

class LupaPassword : Fragment() {
    private var _binding : FragmentLupaPasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var progressDialog :ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding  = FragmentLupaPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Tunggu Sebentar..")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.buttonSubmit.setOnClickListener {
            validateData()
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private var email = ""
    private fun validateData() {
        email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()){
            Toast.makeText(requireContext(),"Isi Email!", Toast.LENGTH_SHORT).show()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(requireContext(),"Format Email Salah!", Toast.LENGTH_SHORT).show()
        }
        else{
            recoverPass()
        }
    }
    private fun recoverPass() {
        progressDialog.setMessage("Link Reset Password Telah Dikirim Ke $email")
        progressDialog.show()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Link Terkirim ke Email!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(requireContext(),"Gagal ${it.message}!", Toast.LENGTH_SHORT).show()
            }
    }

}