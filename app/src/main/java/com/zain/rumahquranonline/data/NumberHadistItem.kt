package com.zain.rumahquranonline.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NumberHadistItem(
    @field:SerializedName("number")
    val number: Int
): Parcelable
