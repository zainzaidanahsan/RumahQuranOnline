package com.zain.rumahquranonline.ui.DoaDoa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zain.rumahquranonline.model.modelDoa.DoaResponseItem
import com.zain.rumahquranonline.model.modelDoa.DoaRetClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoaViewModel : ViewModel() {
    private val _doaList = MutableLiveData<List<DoaResponseItem>>()
    val doaList: LiveData<List<DoaResponseItem>> = _doaList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDoa() {
        _isLoading.value = true // Mulai loading
        DoaRetClient.instance.getDoaList().enqueue(object : Callback<List<DoaResponseItem>> {
            override fun onResponse(
                call: Call<List<DoaResponseItem>>,
                response: Response<List<DoaResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _doaList.value = response.body()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<List<DoaResponseItem>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}