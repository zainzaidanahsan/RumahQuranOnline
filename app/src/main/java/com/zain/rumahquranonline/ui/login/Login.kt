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
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentLoginBinding


class Login : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog : ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.textbelumpunyaakun.setOnClickListener(){
            moveToRegister()
        }

        binding.buttonLogin.setOnClickListener(){
            validateData()
        }

    }

    private var email = ""
    private var password = ""

    private fun validateData() {
        email = binding.editTextEmail.text.toString().trim()
        password = binding.editTextPassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(requireContext(),"Format Email Salah.", Toast.LENGTH_SHORT).show()
        }else if (password.isEmpty()){
            Toast.makeText(requireContext(),"Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }else if (email.isEmpty()){
            Toast.makeText(requireContext(),"Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }else{
            loginUser()
        }
    }

    private fun loginUser() {
        progressDialog.setMessage("Masuk Ke Akun...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {authResult->
                checkUser(authResult.user?.uid)
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                if (e is FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(requireContext(),"Password Salah", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"Login gagal ${e.message}", Toast.LENGTH_SHORT).show()
                }

            }


    }

    private fun checkUser(uid : String?) {
        progressDialog.setMessage("Tunggu sebentar")
        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                progressDialog.dismiss()
                val userType = snapshot.child("userType").value
                if (userType == "user"){
                    moveToHome()
                }else if (userType == "admin"){
                    findNavController().navigate(R.id.action_login_to_homeAdmin)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }

//    private fun setBotNav(){
//        val botNav = activity?.findViewById<BottomAppBar>(R.id.botNavView)
//        botNav?.visibility = View.GONE
//    }

    private fun moveToRegister(){
        findNavController().navigate(R.id.action_login_to_register)
    }
    private fun moveToHome(){
        findNavController().navigate(R.id.action_login_to_home2)
    }
}