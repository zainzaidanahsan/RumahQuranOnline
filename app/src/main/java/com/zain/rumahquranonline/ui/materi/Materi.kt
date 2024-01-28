package com.zain.rumahquranonline.ui.materi

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.adapter.HadistAdapter
import com.zain.rumahquranonline.databinding.FragmentMateriBinding
import com.zain.rumahquranonline.model.modelHadist.Client
import com.zain.rumahquranonline.model.modelHadist.HadistResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Materi : Fragment() {
    private var _binding: FragmentMateriBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HadistAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMateriBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getDataHadist() {
        val apiService = Client.instance

        val call = apiService.getListHadist()
        call.enqueue(object : Callback<List<HadistResponseItem>> {
            override fun onResponse(
                call: Call<List<HadistResponseItem>>,
                response: Response<List<HadistResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val listHadis = response.body()
                    adapter.setData(listHadis!!)
                }
            }

            override fun onFailure(call: Call<List<HadistResponseItem>>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Terjadi kesalahan: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actionBar: androidx.appcompat.app.ActionBar? =
            (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.apply {
            title = "Hadist"
            setBackgroundDrawable(ColorDrawable(Color.WHITE)) // Set background putih
            setDisplayHomeAsUpEnabled(true)
        }
        val backButton: ImageButton = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        recyclerView = binding.rvKategoriMateri
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = HadistAdapter { clickedHadist ->
            val bundle = Bundle().apply {
                putString("collection", clickedHadist.slug)
            }
            val nomorHadistFragment = Nomorhadist()
            nomorHadistFragment.arguments = bundle
            findNavController().navigate(R.id.action_materi_to_nomorhadist, bundle)
        }
        recyclerView.adapter = adapter
        getDataHadist()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
