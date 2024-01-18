package com.zain.rumahquranonline.ui.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentSettingsBinding

class FragmentSettings : Fragment() {
    private var _binding : FragmentSettingsBinding?  = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        loadUserInfo()

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.layoutUser.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSettings_to_profile)
        }
    }

    private fun loadUserInfo(){
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val userType = "${snapshot.child("userType").value}"

                    binding.tvUserType.text = userType
                    binding.tvUserTypeBawah.text = "Kamu Login Sebagai $userType"
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
}