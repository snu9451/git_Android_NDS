package com.example.android_nds.model

import com.google.gson.annotations.SerializedName

data class ItemDetailDto (
    @SerializedName("MEM_EMAIL") val mem_email: String,
    @SerializedName("BM_NO") val bm_no: Int,
    @SerializedName("BI_FILE") val bi_file: String,
    @SerializedName("BM_TITLE") val bm_title: String,
    @SerializedName("BM_DATE") val bm_date: String,
    @SerializedName("BM_PRICE") val bm_price: Int,
    @SerializedName("BM_STATUS") val bm_status: String,
    @SerializedName("BM_CONTENT") val bm_content: String,
    @SerializedName("SELLER_NICKNAME")val seller_nickname: String,
    @SerializedName("I_LIKE")val i_like: Int,
    @SerializedName("BM_HIT")val bm_hit: Int,
    @SerializedName("seller_me")val seller_me:Int,
    @SerializedName("itemImgs") val itemImgs: List<String>,
    @SerializedName("itemComments") val itemComments: List<ItemComments>
    )


/* 예시
{
    "BM_PRICE": 200000,
    "BM_STATUS": "N",
    "itemImgs": [
    "1.png",
    "2.png"
    ],
    "itemComments": [
    {
        "COMMENT_STEP": 1,
        "MEM_NICKNAME": "포도",
        "COMMENT_DATE": "2020-06-17 16:46:41",
        "COMMENT_MSG": "네고 가능한가요?",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 1
    },
    {
        "COMMENT_STEP": 2,
        "MEM_NICKNAME": "바나나",
        "COMMENT_DATE": "2020-06-17 17:46:41",
        "COMMENT_MSG": "안됩니다.",
        "COMMENT_ME": 1,
        "COMMENT_POS": 1,
        "COMMENT_GROUP": 1
    },
    {
        "COMMENT_STEP": 3,
        "MEM_NICKNAME": "멜론",
        "COMMENT_DATE": "2020-06-17 17:56:41",
        "COMMENT_MSG": "ㅋㅋ븅신 그지 새끼냐?",
        "COMMENT_ME": 0,
        "COMMENT_POS": 1,
        "COMMENT_GROUP": 1
    },
    {
        "COMMENT_STEP": 4,
        "MEM_NICKNAME": "수박",
        "COMMENT_DATE": "2020-06-18 17:46:41",
        "COMMENT_MSG": "용량은 얼마나 돼나요?",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 2
    },
    {
        "COMMENT_STEP": 22,
        "MEM_NICKNAME": "토마토",
        "COMMENT_DATE": "2021/06/27 03:31:09",
        "COMMENT_MSG": "호호로로로로로로로롤",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 13
    },
    {
        "COMMENT_STEP": 27,
        "MEM_NICKNAME": "블루베리",
        "COMMENT_DATE": "2021/07/01 17:40:38",
        "COMMENT_MSG": "머냐ㅋㅋㅋ",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 16
    },
    {
        "COMMENT_STEP": 28,
        "MEM_NICKNAME": "블루베리",
        "COMMENT_DATE": "2021/07/01 17:40:48",
        "COMMENT_MSG": "머냐ㅋㅋㅋ",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 17
    },
    {
        "COMMENT_STEP": 31,
        "MEM_NICKNAME": "사과",
        "COMMENT_DATE": "2021/07/01 18:09:00",
        "COMMENT_MSG": "zzz",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 20
    },
    {
        "COMMENT_STEP": 35,
        "MEM_NICKNAME": "사과",
        "COMMENT_DATE": "2021/07/01 18:16:01",
        "COMMENT_MSG": "제발..",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 24
    },
    {
        "COMMENT_STEP": 36,
        "MEM_NICKNAME": "사과",
        "COMMENT_DATE": "2021/07/01 18:19:37",
        "COMMENT_MSG": "되라우",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 25
    },
    {
        "COMMENT_STEP": 38,
        "MEM_NICKNAME": "사과",
        "COMMENT_DATE": "2021/07/01 18:21:46",
        "COMMENT_MSG": "그만하고싶다규",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 27
    },
    {
        "COMMENT_STEP": 41,
        "MEM_NICKNAME": "사과",
        "COMMENT_DATE": "2021/07/01 18:23:01",
        "COMMENT_MSG": "ㅎㅎ",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 30
    },
    {
        "COMMENT_STEP": 46,
        "MEM_NICKNAME": "사과",
        "COMMENT_DATE": "2021/07/01 18:36:16",
        "COMMENT_MSG": "ㅎㅎㅎ",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 35
    },
    {
        "COMMENT_STEP": 47,
        "MEM_NICKNAME": "사과",
        "COMMENT_DATE": "2021/07/01 18:36:29",
        "COMMENT_MSG": "헐 나 된거야?",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 36
    },
    {
        "COMMENT_STEP": 49,
        "MEM_NICKNAME": "사과",
        "COMMENT_DATE": "2021/07/01 18:45:42",
        "COMMENT_MSG": "ㅎㅎㅎ",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 38
    },
    {
        "COMMENT_STEP": 51,
        "MEM_NICKNAME": "사과",
        "COMMENT_DATE": "2021/07/01 18:47:23",
        "COMMENT_MSG": "정말 돼니?",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 40
    },
    {
        "COMMENT_STEP": 56,
        "MEM_NICKNAME": "사과",
        "COMMENT_DATE": "2021/07/01 21:50:36",
        "COMMENT_MSG": "ㅎㅎㅎㅎ",
        "COMMENT_ME": 0,
        "COMMENT_POS": 0,
        "COMMENT_GROUP": 45
    }
    ],
    "BM_TITLE": "핸드폰",
    "BM_NO": 1,
    "BM_CONTENT": "쓰던 핸드폰 팔아요",
    "SELLER_NICKNAME": "바나나",
    "BM_LIKE": 1,
    "BM_HIT": 88,
    "BM_DATE": "2020-06-17 15:46:41",
    "I_LIKE": -1,
    "seller_me": 1
}*/
