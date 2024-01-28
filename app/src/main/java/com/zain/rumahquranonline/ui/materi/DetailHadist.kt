package com.zain.rumahquranonline.ui.materi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zain.rumahquranonline.databinding.FragmentDetailHadistBinding


class DetailHadist : Fragment() {
    private var _binding: FragmentDetailHadistBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailHadistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val hadisNumber = arguments?.getInt("nomor") ?: 0
//        val hadisArab = arguments?.getString("arab") ?: ""
//        val hadisTerjemah = arguments?.getString("terjemahan") ?: ""
//
//        binding.txtNoArab.text = hadisNumber.toString()
//        binding.textArab.text = hadisArab
//        binding.terjemah.text = hadisTerjemah

        val number = arguments?.getInt("number", 0)
        val arab = arguments?.getString("arab")
        val id = arguments?.getString("id")

        binding.txtNoArab.text = number.toString()
        binding.textArab.text = arab
        binding.terjemah.text = id

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}