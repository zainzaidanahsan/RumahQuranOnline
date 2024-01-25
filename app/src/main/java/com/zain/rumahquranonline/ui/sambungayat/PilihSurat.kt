package com.zain.rumahquranonline.ui.sambungayat

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentPilihSuratBinding
import com.zain.rumahquranonline.viewmodel.PilihSuratViewModel


class PilihSurat : BottomSheetDialogFragment() {
    private var _binding: FragmentPilihSuratBinding? = null
    private val binding get() = _binding!!
    private val suratViewModel: PilihSuratViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPilihSuratBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        val listView: ListView = binding.listSurat
        suratViewModel.loadDaftarSurat()
        suratViewModel.daftarSurat.observe(viewLifecycleOwner) { listSurat ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listSurat.map { it.namaLatin ?: "" })
            listView.adapter = adapter
            binding.progressBar.visibility = View.GONE
        }
        suratViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        listView.setOnItemClickListener { _, _, position, _ ->
            val nomorSuratTerpilih = suratViewModel.daftarSurat.value?.get(position)?.nomor ?: return@setOnItemClickListener
            pilihSurat(nomorSuratTerpilih)
        }
    }
    private fun pilihSurat(nomorSurat: Int) {
        val intent = Intent().apply {
            putExtra("nomorSurat", nomorSurat)
        }
        targetFragment?.onActivityResult(targetRequestCode, RESULT_OK, intent)
        dismiss()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}