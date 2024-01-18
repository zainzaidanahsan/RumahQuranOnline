package com.zain.rumahquranonline.ui.home

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentHomeBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler()
    private val updateTimeTask = object : Runnable {
        override fun run() {
            updateClock()
            handler.postDelayed(this, 1000) // Update setiap 1 detik
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler.post(updateTimeTask)
        val calendar = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar)
        binding.hijriDateTextView.text = dateFormat

        binding.cvMenu2.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_listCategoryUser)
        }
        binding.cvMenu1.setOnClickListener(){
            findNavController().navigate(R.id.action_home2_to_pertanyaan)
        }
        binding.cvMenu3.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_setoranHafalan)
        }
        binding.settings.setOnClickListener(){
            findNavController().navigate(R.id.action_home2_to_fragmentSettings)
        }
    }

    private fun updateClock() {
        val currentTime = System.currentTimeMillis()
        val sdf = SimpleDateFormat("HH:mm")
        val timeString = sdf.format(Date(currentTime))
        binding.homeClockTextView.text = timeString
    }

    override fun onResume() {
        super.onResume()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.loginFirebase, false)
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}