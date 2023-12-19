package com.zain.rumahquranonline.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class HadistResponse(
	@field:SerializedName("HadistResponse")
	val hadistResponse: List<HadistResponseItem>
) : Parcelable

@Parcelize
data class HadistResponseItem(
	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("slug")
	val slug: String
) : Parcelable
