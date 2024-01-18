package com.zain.rumahquranonline.model.modelAyat

import com.google.gson.annotations.SerializedName

data class AyatResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AyatItem(

	@field:SerializedName("teksArab")
	val teksArab: String? = null,

	@field:SerializedName("teksLatin")
	val teksLatin: String? = null,

	@field:SerializedName("nomorAyat")
	val nomorAyat: Int? = null,

	@field:SerializedName("audio")
	val audio: Audio? = null,

	@field:SerializedName("teksIndonesia")
	val teksIndonesia: String? = null
)

data class Audio(

	@field:SerializedName("01")
	val jsonMember01: String? = null,

	@field:SerializedName("02")
	val jsonMember02: String? = null,

	@field:SerializedName("03")
	val jsonMember03: String? = null,

	@field:SerializedName("04")
	val jsonMember04: String? = null,

	@field:SerializedName("05")
	val jsonMember05: String? = null
)

data class Data(

	@field:SerializedName("jumlahAyat")
	val jumlahAyat: Int? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("audioFull")
	val audioFull: AudioFull? = null,

	@field:SerializedName("suratSebelumnya")
	val suratSebelumnya: Boolean? = null,

	@field:SerializedName("tempatTurun")
	val tempatTurun: String? = null,

	@field:SerializedName("ayat")
	val ayat: List<AyatItem?>? = null,

	@field:SerializedName("arti")
	val arti: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("suratSelanjutnya")
	val suratSelanjutnya: SuratSelanjutnya? = null,

	@field:SerializedName("nomor")
	val nomor: Int? = null,

	@field:SerializedName("namaLatin")
	val namaLatin: String? = null
)

data class SuratSelanjutnya(

	@field:SerializedName("jumlahAyat")
	val jumlahAyat: Int? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("nomor")
	val nomor: Int? = null,

	@field:SerializedName("namaLatin")
	val namaLatin: String? = null
)

data class AudioFull(

	@field:SerializedName("01")
	val jsonMember01: String? = null,

	@field:SerializedName("02")
	val jsonMember02: String? = null,

	@field:SerializedName("03")
	val jsonMember03: String? = null,

	@field:SerializedName("04")
	val jsonMember04: String? = null,

	@field:SerializedName("05")
	val jsonMember05: String? = null
)
