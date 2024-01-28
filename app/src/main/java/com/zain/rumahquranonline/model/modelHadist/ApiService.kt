package com.zain.rumahquranonline.model.modelHadist

import com.zain.rumahquranonline.data.NumberHadistItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("hadith")
    fun getListHadist(): Call<List<HadistResponseItem>>

    @GET("hadith/{collection}")
    fun getNomorHadist(@Path("collection") collection: String): Call<List<NumberHadistItem>>

    @GET("hadith/{collection}/{id}")
    fun getDetailHadist(
        @Path("collection") collection: String,
        @Path("id") id: Int
    ): Call<DetailHadistResponse>
}