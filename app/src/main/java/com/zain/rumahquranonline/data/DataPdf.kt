package com.zain.rumahquranonline.data

data class DataPdf(
    val uid: String = "",
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val categoryID: String = "",
    val url: String = "",
    val timestamp: Long = 0,
    val viewsCount: Long = 0,
    val downloadsCount : Long = 0
    )
