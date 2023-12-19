package com.zain.rumahquranonline.ui.Profile

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentLoginFirebaseBinding
import com.zain.rumahquranonline.databinding.FragmentProfileBinding

class Profile : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding.txtKeluar.setOnClickListener{
            signOut()
        }

    }

    private fun signOut(){
        auth.signOut()
        findNavController().navigate(R.id.action_profile_to_loginFirebase)
    }



}