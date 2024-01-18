package com.zain.rumahquranonline.model.modelAyat

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApiService {
    @GET("v2/surat/{number}")
    fun getAyat(@Path("number") number: Int): Call<AyatResponse>
}