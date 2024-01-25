package com.zain.rumahquranonline.model.modelDoa

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DoaRetClient {
    private const val BASE_URL = "https://doa-doa-api-ahmadramadhan.fly.dev"

    val instance: DoaApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DoaApiService::class.java)
    }
}