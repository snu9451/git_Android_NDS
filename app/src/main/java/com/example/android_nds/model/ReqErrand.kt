package com.example.android_nds.model

import android.util.Log
// DEPRECATED
class ReqErrand {
    constructor(){
        Log.i("mymymy", "ReqErrand 생성자 호출 완료")
    }
    constructor(
        errandKey: String,
        errand_content: String,
        errand_item: String,
        errand_item_price_req: String,
        errand_lat: Any,
        errand_lng: Any,
//        errand_lat: String,
//        errand_lng: String,
        errand_price: String,
        errand_total_price: Int,
        errand_request_date: String,
        status: Int,
        mem_email: String,
        rider: String
    ) : this() {
        Log.i("mymymy","ReqErrand 생성자 호출 완료2222")
        this.errandKey = errandKey
        this.errand_content = errand_content
        this.errand_item = errand_item
        this.errand_item_price_req = errand_item_price_req
        this.errand_lat = errand_lat
        this.errand_lng = errand_lng
        this.errand_price = errand_price
        this.errand_total_price = errand_total_price
        this.errand_request_date = errand_request_date
        this.status = status
        this.mem_email = mem_email
        this.rider = rider
    }
    var errandKey: String = ""
    var errand_content: String = ""
    var errand_item: String = ""
    var errand_item_price_req: String = ""
    var errand_lat: Any? = null
    var errand_lng: Any? = null
//    var errand_lat: String = ""
//    var errand_lng: String = ""
    var errand_price: String = ""
    var errand_total_price: Int = 0
    var errand_request_date: String = ""
    var status: Int = 0
    var mem_email: String = ""
    var rider: String = ""
}