package com.zain.rumahquranonline.ui.sambungayat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentChatBinding
import com.zain.rumahquranonline.databinding.FragmentPertanyaanBinding
import com.zain.rumahquranonline.viewmodel.PertanyaanViewModel


class Pertanyaan : Fragment() {
    private lateinit var viewModel: PertanyaanViewModel
    private var _binding : FragmentPertanyaanBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPertanyaanBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PertanyaanViewModel::class.java)

        viewModel.currentAyat.observe(viewLifecycleOwner) { ayat ->
            binding.tvAyatNumber.text = ayat.nomorAyat.toString()
            binding.tvAyatText.text = ayat.teksArab
        }
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioJawaban)
        viewModel.answerOptions.observe(viewLifecycleOwner) { options ->
            radioGroup.removeAllViews() // Clear previous options
            options.forEachIndexed { index, option ->
                val radioButton = RadioButton(context).apply {
                    text = option
                    id = View.generateViewId()
                    setPadding(16, 16, 16, 16)
                    layoutParams = RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT).apply {
                        bottomMargin = 10
                    }
                    background = context?.getDrawable(R.drawable.rounded_message_yellow)
                    setTextColor(resources.getColor(R.color.black))
                }
                radioGroup.addView(radioButton)
                if (index == 0) radioGroup.check(radioButton.id)
                binding.btnNext.isEnabled = true
            }
        }
        // Load ayat ketika Fragment dimulai
        viewModel.loadAyat(1) // Angka 1 adalah contoh untuk surat Al-Fatihah
        binding.btnNext.isEnabled = false // Disable the button initially
        binding.btnNext.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            val selectedAnswer = view.findViewById<RadioButton>(selectedRadioButtonId).text.toString()
            // Log untuk debugging
            Log.d("AyatFragment", "Selected Answer: $selectedAnswer")
            Log.d("AyatFragment", "Correct Answer: ${viewModel.correctAnswer}")
            val isCorrect = viewModel.checkAnswer(selectedAnswer)
            if (isCorrect) {
                Toast.makeText(context, "Jawaban Anda benar!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Jawaban Anda salah, coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}