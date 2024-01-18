package com.zain.rumahquranonline.model.modelAyat

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val api: QuranApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://equran.id/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuranApiService::class.java)
    }
}