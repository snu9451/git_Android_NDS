package com.example.android_nds.api

import com.example.android_nds.model.MemberVO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MemberService {
    //받아오는 주소와 output은 json형태
    //전체 다 가져오는 애
    @FormUrlEncoded
    @POST("/member/doLoginAndroid.nds")
    fun selectIsMember(
//        @Body memberVO: MemberVO
        @Field("mem_email") memEmail: String,
        @Field("mem_pw") memPw: String
    ): Call<String>

}