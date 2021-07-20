package com.example.android_nds.model

import com.google.gson.annotations.SerializedName

data class ItemComments (
    @SerializedName("COMMENT_STEP") val comment_step: Int,
    @SerializedName("MEM_NICKNAME") val mem_nickname: String,
    @SerializedName("COMMENT_DATE") val comment_date: String,
    @SerializedName("COMMENT_MSG") val comment_msg: String,
    @SerializedName("COMMENT_ME") val comment_me: Int,
    @SerializedName("COMMENT_POS") val comment_pos: Int,
    @SerializedName("COMMENT_GROUP") val comment_group: Int
        )

/*
"COMMENT_STEP": 1,
"MEM_NICKNAME": "포도",
"COMMENT_DATE": "2020-06-17 16:46:41",
"COMMENT_MSG": "네고 가능한가요?",
"COMMENT_ME": 0,
"COMMENT_POS": 0,
"COMMENT_GROUP": 1
*/
