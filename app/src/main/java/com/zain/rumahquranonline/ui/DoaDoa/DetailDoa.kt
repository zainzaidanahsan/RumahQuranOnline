package com.zain.rumahquranonline.ui.DoaDoa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentDetailDoaBinding
import com.zain.rumahquranonline.databinding.FragmentDoaBinding
import com.zain.rumahquranonline.model.modelDoa.DoaResponseItem

class DetailDoa : Fragment() {
    private var _binding: FragmentDetailDoaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentDetailDoaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val doaItem = arguments?.getSerializable("doaItem") as? DoaResponseItem


        doaItem?.let {
            // menampilkan detail doa
            binding.judulDoa.text = doaItem.doa
            binding.arabicText.text= doaItem.ayat
            binding.transliterationText.text= doaItem.latin
            binding.translationText.text=doaItem.artinya
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}