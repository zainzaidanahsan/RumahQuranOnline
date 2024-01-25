package com.zain.rumahquranonline.model.modelAdzan

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AdzanApiService {
    @GET("sholat/kota/semua")
    fun getAllCities(): Call<DataKotaResponse>

    @GET("sholat/kota/cari/{query}")
    fun searchCity(@Path("query") query: String): Call<DataKotaResponse>

    @GET("sholat/jadwal/{cityId}/{year}/{month}/{day}")
    fun getPrayerTimes(
        @Path("cityId") cityId: String,
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Path("day") day: Int
    ): Call<WaktuAdzanResponse>
}