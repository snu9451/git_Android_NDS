package com.example.android_nds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity(){

    val TAG: String = "mymymy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    // 회원가입 버튼 눌렀을 때의 이벤트
    // private으로 하면 찾을 수 없음
   fun join(view: View) {
        Log.i(TAG,"[회원가입] 버튼 클릭 완료 - 액티비티 전환")
        var intent = Intent(this, JoinActivity::class.java)
        startActivity(intent)
    }

    // 로그인 버튼 눌렀을 때의 이벤트
   fun login(view: View) {
        Log.i(TAG,"[로그인] 버튼 클릭 완료 - 액티비티 전환")
        // 회원 아이디와 비번 비교하는 작업 - inser_here
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}