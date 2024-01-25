package com.zain.rumahquranonline.model.modelAyat

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val gson = GsonBuilder()
        .registerTypeAdapter(SuratSelanjutnya::class.java, SuratSelanjutnyaDeserializer())
        .registerTypeAdapter(SuratSebelumnya::class.java, SuratSebelumnyaDeserializer())
        .create()

    val api: QuranApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://equran.id/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(QuranApiService::class.java)
    }
}