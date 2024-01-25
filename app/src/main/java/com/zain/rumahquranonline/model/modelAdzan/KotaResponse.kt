package com.zain.rumahquranonline.model.modelAdzan

import com.google.gson.annotations.SerializedName

data class KotaResponse(

	@field:SerializedName("lokasi")
	val lokasi: String,

	@field:SerializedName("id")
	val id: String
)
