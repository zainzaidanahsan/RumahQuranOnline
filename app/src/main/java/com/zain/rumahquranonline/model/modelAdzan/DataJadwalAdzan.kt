package com.zain.rumahquranonline.model.modelAdzan

data class DataJadwalAdzan(
    val id: Int,
    val lokasi: String,
    val daerah: String,
    val jadwal: JadwalAdzanResponse
)
