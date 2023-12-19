package com.zain.rumahquranonline.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Materi(
    var judul :  String,
    var isi : String,
    var icon: Int
): Parcelable
