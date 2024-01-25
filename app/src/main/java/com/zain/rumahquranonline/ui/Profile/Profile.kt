package com.zain.rumahquranonline.ui.Profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zain.rumahquranonline.R

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
        loadUserInfo()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.cvEdit.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_editProfile)

        }

    }

    private fun signOut(){
        auth.signOut()
        findNavController().navigate(R.id.action_profile_to_pilihRole)
    }

    private fun loadUserInfo(){
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = "${snapshot.child("email").value}"
                    val username = "${snapshot.child("username").value}"
                    val imageProfile = "${snapshot.child("photoImage").value}"
                    val timestamp ="${snapshot.child("timestamp").value}"
                    val uid = "${snapshot.child("uid").value}"
                    val userType = "${snapshot.child("userType").value}"

                    binding.tvUsername.text = username
                    binding.tvNameUser.text = username
                    binding.tvEmailUser.text =  email
                    try {
                        Glide.with(requireActivity())
                            .load(imageProfile)
                            .placeholder(R.drawable.baseline_add_a_photo_24)
                            .into(binding.addPhoto)
                    }catch (e:Exception){
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
    override fun onResume() {
        super.onResume()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.pilihRole, false)
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}