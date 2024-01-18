package com.zain.rumahquranonline.ui.setoranhafalan

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.adapter.adapteruser.JadwalAdapter
import com.zain.rumahquranonline.data.Jadwal
import com.zain.rumahquranonline.databinding.FragmentCreateScheduleBinding
import com.zain.rumahquranonline.databinding.FragmentSetoranHafalanBinding

class SetoranHafalan : Fragment() {
    private var _binding : FragmentSetoranHafalanBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var progressDialog: ProgressDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var jadwalList: MutableList<Jadwal>
    private lateinit var adapter: JadwalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSetoranHafalanBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvJadwal)
        jadwalList = mutableListOf()
        adapter = JadwalAdapter(jadwalList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadJadwal()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_setoranHafalan_to_chat)
        }
    }

    private fun loadJadwal() {
        val database = FirebaseDatabase.getInstance().getReference("jadwal")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                jadwalList.clear()
                for (jadwalSnapshot in snapshot.children) {
                    val jadwal = jadwalSnapshot.getValue(Jadwal::class.java)
                    jadwal?.let { jadwalList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}