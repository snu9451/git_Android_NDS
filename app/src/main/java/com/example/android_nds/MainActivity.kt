package com.example.android_nds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MyMainActivityNDS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG,"여기여기")
        // https://androidnds-9ac2f-default-rtdb.asia-southeast1.firebasedatabase.app/
        writeToFB()
    }

    fun writeToFB(){
        Log.i(TAG,"writeToFB 호출 완료")
        // DB에 메세지 쓰기
        val database = Firebase.database("https://androidnds-9ac2f-default-rtdb.asia-southeast1.firebasedatabase.app/")
        Log.i(TAG,"$database")
//        val myRef = database.getReference("message")
        val myRef = database.getReference() // base위치를 가리킴(base위치까지 진입하게 됨): androidnds-9ac2f-default-rtdb
        Log.i(TAG,"$myRef")
//        Log.i(TAG,"$myRef")
//        myRef.setValue("local")
        val myChild = myRef.child("first")
        val myChild2 = myRef.child("first/second")
        Log.i(TAG,"$myChild")
        Log.i(TAG,"$myChild2")
        Log.i(TAG, "${myChild.get()}")
//        myRef.setValue("local") // 최상단의(base의) ref에다가 대고 setValue 해버리면 아래 자식들 다 사라지고 값만 세팅됨...

    }
}