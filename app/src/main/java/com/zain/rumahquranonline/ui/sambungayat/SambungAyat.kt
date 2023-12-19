package com.zain.rumahquranonline.ui.sambungayat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.adapter.HadistAdapter
import com.zain.rumahquranonline.adapter.SambungAyatAdapter
import com.zain.rumahquranonline.data.JuzSambungAyat
import com.zain.rumahquranonline.databinding.FragmentProfileBinding
import com.zain.rumahquranonline.databinding.FragmentSambungAyatBinding


class SambungAyat : Fragment() {
    private var _binding: FragmentSambungAyatBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSambungAyatBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvSambungayat
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val data = ArrayList<JuzSambungAyat>()

        for (i in 1..30) {
            data.add(JuzSambungAyat(R.drawable.book, "Juz " + i))
        }
        val adapter = SambungAyatAdapter(data)
        recyclerView.adapter = adapter

    }
}