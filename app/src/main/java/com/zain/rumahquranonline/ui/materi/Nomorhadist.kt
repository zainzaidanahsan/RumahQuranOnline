package com.zain.rumahquranonline.ui.materi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zain.rumahquranonline.adapter.NomorHadistAdapter
import com.zain.rumahquranonline.databinding.FragmentNomorhadistBinding
import com.zain.rumahquranonline.model.modelHadist.DetailHadistResponse


class Nomorhadist : Fragment() {

    private var _binding: FragmentNomorhadistBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNomorhadistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.rvNoHadist
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val detailHadistList: List<DetailHadistResponse> = getYourHadistData()

        // Ambil hanya nomor hadis
        val nomorHadistList: List<Int> = detailHadistList.map { it.number }
        val adapter = NomorHadistAdapter(nomorHadistList, detailHadistList)
        recyclerView.adapter = adapter

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getYourHadistData(): List<DetailHadistResponse> {
        val hadisList = ArrayList<DetailHadistResponse>()

        for (i in 1..1000) {
            hadisList.add(
                DetailHadistResponse(
                    i,
                    "Abu Dawud",
                    "id$i",
                    "slug$i",
                    "Hadis Arab $i"
                )
            )
        }

        return hadisList
    }
}