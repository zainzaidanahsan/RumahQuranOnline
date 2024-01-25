package com.zain.rumahquranonline.ui.pilihRole

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentChatBinding
import com.zain.rumahquranonline.databinding.FragmentPilihRoleBinding

class PilihRole : Fragment() {

    private var _binding : FragmentPilihRoleBinding? = null
    private val binding get() = _binding!!

    private var selectedRole: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPilihRoleBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardStudent.setOnClickListener {
            selectedRole = "student"
            updateCardSelection()
        }

        binding.cardTeacher.setOnClickListener {
            selectedRole = "teacher"
            updateCardSelection()
        }
        binding.btnLogin.isEnabled = false

        binding.btnLogin.setOnClickListener {
            when (selectedRole) {
                "student" -> {
                    // Navigasi ke Fragment Login
                    findNavController().navigate(R.id.action_pilihRole_to_login)
                }
                "teacher" -> {
                    // Tampilkan dialog
                    showDevelopmentDialog()
                }
            }
        }
    }
    private fun updateCardSelection() {
        binding.cardStudent.setCardBackgroundColor(
            if (selectedRole == "student")
                ContextCompat.getColor(requireContext(), R.color.primarycolour)
            else
                ContextCompat.getColor(requireContext(), R.color.white)
        )

        binding.cardTeacher.setCardBackgroundColor(
            if (selectedRole == "teacher")
                ContextCompat.getColor(requireContext(), R.color.primarycolour)
            else
                ContextCompat.getColor(requireContext(), R.color.white)
        )

        binding.btnLogin.isEnabled = selectedRole != null
        binding.tvSignUp.setOnClickListener {

        }
    }
    private fun showDevelopmentDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Masih Dalam Pengembangan")
            .setMessage("Maaf, fitur untuk role guru saat ini masih dalam tahap pengembangan.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}