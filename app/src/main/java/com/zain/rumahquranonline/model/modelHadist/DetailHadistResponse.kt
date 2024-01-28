package com.zain.rumahquranonline.model.modelHadist

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DetailHadistResponse(

	@field:SerializedName("number")
	val number: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("slug")
	val slug: String,

	@field:SerializedName("arab")
	val arab: String
) : Parcelable
