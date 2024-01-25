package com.zain.rumahquranonline.model.modelDoa

import retrofit2.Call
import retrofit2.http.GET

interface DoaApiService {
    @GET("/api")
    fun getDoaList(): Call<List<DoaResponseItem>>
}