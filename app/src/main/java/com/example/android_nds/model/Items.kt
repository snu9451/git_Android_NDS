package com.example.android_nds.model

import com.google.gson.annotations.SerializedName

data class Items (
    @SerializedName("BM_NO") val bm_no: Int,
    @SerializedName("BI_FILE") val bi_file: String,
    @SerializedName("BM_TITLE") val bm_title: String,
    @SerializedName("BM_DATE") val bm_date: String,
    @SerializedName("BM_PRICE") val bm_price: Int
)