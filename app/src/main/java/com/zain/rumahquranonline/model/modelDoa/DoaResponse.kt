package com.zain.rumahquranonline.model.modelDoa

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DoaResponse(

	@field:SerializedName("DoaResponse")
	val doaResponse: List<DoaResponseItem>
)

data class DoaResponseItem(

	@field:SerializedName("ayat")
	val ayat: String,

	@field:SerializedName("doa")
	val doa: String,

	@field:SerializedName("artinya")
	val artinya: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("latin")
	val latin: String
):Serializable
