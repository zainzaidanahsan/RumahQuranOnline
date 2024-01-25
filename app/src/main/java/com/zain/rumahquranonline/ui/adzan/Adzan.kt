package com.zain.rumahquranonline.ui.adzan

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zain.rumahquranonline.R
import com.zain.rumahquranonline.databinding.FragmentAdzanBinding
import com.zain.rumahquranonline.databinding.FragmentDoaBinding
import com.zain.rumahquranonline.model.modelAdzan.AdzanClient
import com.zain.rumahquranonline.model.modelAdzan.DataKotaResponse
import com.zain.rumahquranonline.model.modelAdzan.KotaResponse
import com.zain.rumahquranonline.ui.DoaDoa.DoaViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.Calendar

class Adzan : Fragment() {
    private var _binding: FragmentAdzanBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AdzanViewModel
    private lateinit var cities: List<KotaResponse>
    private var selectedCityId: String? = null

    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdzanBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdzanViewModel::class.java)
        autoCompleteTextView = view.findViewById(R.id.locationSpinner)
        loadCities()
        observePrayerTimes()
        val selectedCityId = loadSelectedCityID()
        val selectedCityName = loadSelectedCityName()
        if (selectedCityId != null) {
            loadPrayerTimesForCity(selectedCityId)
        }
        if (selectedCityId != null) {
            autoCompleteTextView.setText(selectedCityName, false)
        }

        autoCompleteTextView.setOnItemClickListener{ _, _, position, _ ->
            val cityId = cities[position].id
            val cityName = cities[position].lokasi
            saveSelectedCity(cityId, cityName)
            loadPrayerTimesForCity(cityId)
        }

//        binding.btnBack.setOnClickListener {
//            findNavController().popBackStack()
//        }
    }


    private fun loadCities() {
        AdzanClient.api.getAllCities().enqueue(object : Callback<DataKotaResponse> {
            override fun onResponse(call: Call<DataKotaResponse>, response: Response<DataKotaResponse>) {
                if (isAdded) {
                    cities = response.body()?.data ?: emptyList()
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities.map { it.lokasi })
                    autoCompleteTextView.setAdapter(adapter)
                }
            }

            override fun onFailure(call: Call<DataKotaResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
    private fun observePrayerTimes() {
        viewModel.prayerTimes.observe(viewLifecycleOwner, Observer { data ->
            Log.d("AdzanFragment", "Observer called with data: $data")
            data?.jadwal?.let { jadwal ->
                binding.imsakTime.text = jadwal.imsak
                binding.subuhTime.text = jadwal.subuh
                binding.syuruqTime.text = jadwal.terbit
                binding.dzuhurTime.text = jadwal.dzuhur
                binding.asharTime.text = jadwal.ashar
                binding.maghribTime.text = jadwal.maghrib
                binding.isyaTime.text = jadwal.isya
            }
        })
    }
    private fun loadSelectedCityID(): String? {
        val sharedPref = activity?.getSharedPreferences("AdzanPreferences", Context.MODE_PRIVATE)
        return sharedPref?.getString("SELECTED_CITY_ID", null)
    }
    private fun loadSelectedCityName(): String? {
        val sharedPref = activity?.getSharedPreferences("AdzanPreferences", Context.MODE_PRIVATE)
        return sharedPref?.getString("SELECTED_CITY_NAME", null)
    }
    private fun saveSelectedCity(cityId: String, cityName: String) {
        val sharedPref = activity?.getSharedPreferences("AdzanPreferences", Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("SELECTED_CITY_ID", cityId)
            putString("SELECTED_CITY_NAME", cityName)
            apply()
        }
    }


    private fun loadPrayerTimesForCity(cityId: String) {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH) + 1
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        viewModel.loadPrayerTimes(cityId, year, month, day)
    }
}