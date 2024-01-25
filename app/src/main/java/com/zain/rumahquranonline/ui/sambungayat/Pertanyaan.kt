package com.zain.rumahquranonline.ui.sambungayat

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentChatBinding
import com.zain.rumahquranonline.databinding.FragmentPertanyaanBinding
import com.zain.rumahquranonline.viewmodel.PertanyaanViewModel


class Pertanyaan : Fragment() {
    private lateinit var viewModel: PertanyaanViewModel
    private var _binding : FragmentPertanyaanBinding? = null
    private val binding get() = _binding!!
    private var nomorSuratTerpilih: Int? = null

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
        if (nomorSuratTerpilih == null) {
            showPilihSuratDialog()
        } else {
            loadNewQuestion(nomorSuratTerpilih!!)
        }

        viewModel.currentAyat.observe(viewLifecycleOwner) { ayat ->
            binding.tvAyatNumber.text = ayat.nomorAyat.toString()
            binding.tvAyatText.text = ayat.teksArab
            binding.tvAyatLatin.text = ayat.teksLatin
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
                    background = context?.getDrawable(R.drawable.background_jawaban)
                    setTextColor(resources.getColor(R.color.black))
                    typeface = ResourcesCompat.getFont(context, R.font.majalla)
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f) // 18sp
                }
                radioGroup.addView(radioButton)
                if (index == 0) radioGroup.check(radioButton.id)
                binding.btnJawab.isEnabled = true
            }
        }

        binding.btnJawab.isEnabled = false // Disable the button initially
        binding.btnJawab.setOnClickListener {
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            val selectedAnswer = view.findViewById<RadioButton>(selectedRadioButtonId).text.toString()
            // Log untuk debugging
            Log.d("AyatFragment", "Selected Answer: $selectedAnswer")
            Log.d("AyatFragment", "Correct Answer: ${viewModel.correctAnswer}")
            val isCorrect = viewModel.checkAnswer(selectedAnswer)
            if (isCorrect) {
                nomorSuratTerpilih?.let { loadNewQuestion(it) }
                Toast.makeText(context, "Jawaban Anda benar!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Jawaban Anda salah, coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadNewQuestion(suratNumber: Int) {
        viewModel.loadAyat(suratNumber)
        binding.btnJawab.isEnabled = false // Nonaktifkan tombol sampai jawaban dimuat
    }

    private fun showPilihSuratDialog() {
        val pilihSuratFragment = PilihSurat().apply {
            setTargetFragment(this@Pertanyaan, 1)
        }
        pilihSuratFragment.show(parentFragmentManager, "PilihSuratFragment")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            nomorSuratTerpilih = data?.getIntExtra("nomorSurat", -1) ?: -1
            if (nomorSuratTerpilih != -1) {
                loadNewQuestion(nomorSuratTerpilih!!)
            }
        }
    }
    override fun onStop() {
        super.onStop()
        // Reset nomorSuratTerpilih ketika fragment tidak lagi terlihat
        nomorSuratTerpilih = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}