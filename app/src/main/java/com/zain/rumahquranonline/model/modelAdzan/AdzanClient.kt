package com.zain.rumahquranonline.model.modelAdzan

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AdzanClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.myquran.com/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: AdzanApiService = retrofit.create(AdzanApiService::class.java)
}