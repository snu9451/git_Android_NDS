package com.example.android_nds

import android.util.Log
import com.example.android_nds.model.ReqErrand
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NDSFirebaseSync {

    val TAG:String = "mymymy"
    var allErrandMap: MutableMap<String, Map<String, String>>
    lateinit var errandLocationMap: MutableMap<String, String>

    // 파이어베이스 연동에 필요한 선언
    private var database: FirebaseDatabase
    private lateinit var reqErrandRef: DatabaseReference

    constructor(){
        database = Firebase.database
        allErrandMap = HashMap()
    }

    // 파이어베이스에서 데이터 가져오기, 이벤트 달기
    fun initFirebase() {
        database = Firebase.database
        Log.i(TAG, "database: $database")
        reqErrandRef = database.getReference("area")
        Log.i(ReqErrandFragment.TAG, "reqErrandRef: $reqErrandRef")
        // ChildEventListener 생성하기
        val reqErrandChildAddListener = object: ChildEventListener {
            // onChildAdded는 최초 1회 데이터를 가져옴. 이후로는 추가될 때 가져옴.
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i(ReqErrandFragment.TAG, "onChildAdded 호출 성공")
                Log.i(ReqErrandFragment.TAG, "정보 ===> ${snapshot.child("errand").apply {
                    Log.i(ReqErrandFragment.TAG, "this.value: "+this.value.toString())
                    if(this.value!=null) this.children.forEach{ it ->
                        Log.i(TAG, "각 심부름의 key: "+it.key.toString())
                        Log.i(TAG, "ReqErrand에 담길 정보: "+it.value.toString())
                        // 파이어베이스의 데이터 정보를 준비해 둔 ReqErrand 객체에 매핑
//                        val reqErrandModel: ReqErrand? = it.getValue(ReqErrand::class.java)
//                        val reqErrandModel: Map<String, Object> = HashMap<String, Object>().toMutableMap()
                        errandLocationMap = HashMap()
                        errandLocationMap["errandKey"] = it.key.toString()
                        it.children.forEach { 
//                            Log.i(TAG, "HERE!!!! ==> ${it.value}")
//                            if("errand_lat" == it.key) Log.i(TAG, "errand_lat: ${it.value}")
                            errandLocationMap[it.key.toString()] = it.value.toString()
                        }
//                        Log.i(TAG, "errand_lat: ${reqErrandModel["errand_lat"]}")
//                        Log.i(TAG, "errand_lat: ${reqErrandModel["errand_lng"]}")
//                        Log.i(TAG, "errand_lng: ${reqErrandModel?.errand_lng}")
//                        errandLocationMap["errand_lat"] =
//                            reqErrandModel?.errand_lat.toString()
//                            reqErrandModel["errand_lat"].toString()
//                        errandLocationMap["errand_lng"] =
//                            reqErrandModel?.errand_lng.toString()
//                            reqErrandModel["errand_lng"].toString()
                        allErrandMap[it.key.toString()] = errandLocationMap
                    }
                }}")
//                Log.i(TAG,"previousChildName: ${previousChildName}")    // previousChildName: 같은 깊이에서 자기 보다 앞에 있는 키의 이름임

            }/////////////////////end of onChildAdded


            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        reqErrandRef.addChildEventListener(reqErrandChildAddListener)
    }

}