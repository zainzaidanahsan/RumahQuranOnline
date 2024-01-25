package com.zain.rumahquranonline.ui.schedule

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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
            val namaUstadz = binding.ustadzNameEditText.text.toString()

            if (namaUstadz.isNotEmpty()) {
                val newJadwal = Jadwal(selectedDate, selectedSesi, namaUstadz)
                saveJadwalToFirebase(newJadwal)
            } else {
                Toast.makeText(context, "Masukkan nama ustadz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveJadwalToFirebase(jadwal: Jadwal) {
        progressDialog.setMessage("Membuat Jadwal...")
        progressDialog.show()
        val jadwalRef = database.child("jadwal")
        val key = jadwalRef.push().key
        key?.let {
            // Set ID jadwal
            jadwal.id = key

            jadwalRef.child(key).setValue(jadwal)
                .addOnSuccessListener {
                    createChatRoom(key)
                    progressDialog.dismiss()
                    Toast.makeText(context, "Jadwal Berhasil Dibuat.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Jadwal Gagal Dibuat karena ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun createChatRoom(jadwalId: String) {
        val chatRoomRef = database.child("chatRooms").child(jadwalId)
        val chatRoom = HashMap<String, Any>()
        chatRoom["id"] = jadwalId

        chatRoomRef.setValue(chatRoom)
            .addOnSuccessListener {
                // Chat room berhasil dibuat
            }
            .addOnFailureListener {
                // Gagal membuat chat room
            }    }

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
