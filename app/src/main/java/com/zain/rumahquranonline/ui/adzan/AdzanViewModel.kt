package com.zain.rumahquranonline.ui.adzan

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zain.rumahquranonline.model.modelAdzan.AdzanClient
import com.zain.rumahquranonline.model.modelAdzan.DataJadwalAdzan
import com.zain.rumahquranonline.model.modelAdzan.JadwalAdzanResponse
import com.zain.rumahquranonline.model.modelAdzan.WaktuAdzanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdzanViewModel: ViewModel() {
    val prayerTimes: MutableLiveData<DataJadwalAdzan?> = MutableLiveData()

    fun loadPrayerTimes(cityId: String, year: Int, month: Int, day: Int) {
        AdzanClient.api.getPrayerTimes(cityId, year, month, day).enqueue(object :
            Callback<WaktuAdzanResponse> {
            override fun onResponse(
                call: Call<WaktuAdzanResponse>,
                response: Response<WaktuAdzanResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == true && responseBody.data != null) {
                        prayerTimes.postValue(responseBody.data)
                    } else {
                        Log.e("AdzanViewModel", "Data or Jadwal is null: $responseBody")
                    }
                } else {
                    Log.e("AdzanViewModel", "Response Error: ${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<WaktuAdzanResponse>, t: Throwable) {
                Log.e("AdzanViewModel", "Network Error", t)
            }
        })
    }

//    fun testLoadPrayerTimes() {
//        val dummyData = DataJadwalAdzan(
//            id = 1609,
//            lokasi = "KAB. KEDIRI",
//            daerah = "JAWA TIMUR",
//            jadwal = JadwalAdzanResponse(
//                tanggal = "Senin, 22/01/2024",
//                imsak = "03:57",
//                isya = "19:02",
//                maghrib = "18:20",
//                dzuhur = "12:20",
//                ashar = "15:23",
//                terbit = "06:00",
//                subuh = "04:32",
//                dhuha = "09:12",
//                date = "22/02/2024"
//            )
//        )
//        prayerTimes.postValue(dummyData)
//    }

}