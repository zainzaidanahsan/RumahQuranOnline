package com.zain.rumahquranonline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zain.rumahquranonline.model.modelAyat.Data
import com.zain.rumahquranonline.model.modelAyat.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class PilihSuratViewModel: ViewModel() {
    private val _daftarSurat = MutableLiveData<List<Data>>()
    val daftarSurat: LiveData<List<Data>> = _daftarSurat
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadDaftarSurat() {
        _isLoading.value = true // Mulai loading
        viewModelScope.launch {
            // Contoh, anggap setiap surat tersedia dari 1 sampai 114
            val listSurat = (1..114).map { nomor ->
                RetrofitClient.api.getAyat(nomor).awaitResponse().body()?.data
            }.filterNotNull()
            _daftarSurat.value = listSurat
            _isLoading.value = false // Selesai loading
        }
    }
}