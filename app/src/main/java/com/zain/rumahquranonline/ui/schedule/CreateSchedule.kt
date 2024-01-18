package com.zain.rumahquranonline.ui.schedule

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.data.Jadwal
import com.zain.rumahquranonline.databinding.FragmentCreateScheduleBinding
import com.zain.rumahquranonline.utils.Utils

class CreateSchedule : Fragment() {
    private var _binding : FragmentCreateScheduleBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateScheduleBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        database = FirebaseDatabase.getInstance().reference
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Tunggu")
        progressDialog.setCanceledOnTouchOutside(false)

        setupButton()



    }

    private fun setupButton() {
        binding.scheduleButton.setOnClickListener {
            val selectedDate = "${binding.datePicker.dayOfMonth}/${binding.datePicker.month + 1}/${binding.datePicker.year}"
            val selectedSesi = binding.timeSpinner.selectedItem.toString()

            val newJadwal = Jadwal(selectedDate, selectedSesi)
            saveJadwalToFirebase(newJadwal)
        }
    }

    private fun saveJadwalToFirebase(jadwal: Jadwal) {
        progressDialog.setMessage("Membuat Jadwal...")
        progressDialog.show()
        val key = database.child("jadwal").push().key
        if (key != null){
            database.child("jadwal").child(key).setValue(jadwal)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Utils.toast(requireContext(),"Jadwal Berhasil Dibuat.")
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Utils.toast(requireContext(),"Jadwal Gagal Dibuat karena ${it.message}")
                }
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.jam_mengajar_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.timeSpinner.adapter = adapter
        }
    }
}
