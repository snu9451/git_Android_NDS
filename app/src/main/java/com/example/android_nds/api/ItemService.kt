package com.example.android_nds.api

import com.example.android_nds.model.ItemDetailDto
import com.example.android_nds.model.Items
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ItemService {
    //받아오는 주소와 output은 json형태
    //전체 다 가져오는 애
    @GET("/item/andselectItemList.nds?")
    fun getItemByName(
        @Query("pr_choice") pr_choice: String
    ): Call<List<Items>>

    //받아오는 주소와 output은 json형태
    //검색된 애들만 가져오는 애
    @GET("/item/andselectBySearch.nds?")
    fun getItemSearchByName(
        @Query("pr_search") pr_search: String
    ): Call<List<Items>>

    //아이템 디테일 보기기
   @GET("/item/andselectItemDetail.nds?")
    fun getItemDetailByName(
        @Query("pr_bm_no") pr_bm_no: Int
    ): Call<ItemDetailDto>

    //댓글 삭제
   @POST("/item/andDeleteComment.nds")
    fun getCommentDeleteByName(
        @Query("p_comment_step") comment_step: Int
    ): Call<ResponseBody>

    //댓글 달기
   @POST("/item/andInsertComment.nds")
    fun getCommentInsertByName(
        @Query("pr_comment_msg") comment: String,
        @Query("pr_comment_group") group: Int,
        @Query("pr_comment_pos") pos: Int,
        @Query("pr_bm_no") bm_no: Int
    ): Call<ResponseBody>
}