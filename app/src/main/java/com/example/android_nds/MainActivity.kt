package com.example.android_nds

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MyMainActivityNDS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG,"여기여기")
        // https://androidnds-9ac2f-default-rtdb.asia-southeast1.firebasedatabase.app/
//        writeToFB()

        // 프래그먼트의 초기화
        val reqErrandFragment = ReqErrandFragment()
        val mapFragment = MapFragment()

        // Navigation Menu를 클릭시 프래그먼트 교체 또는 액티비티 이동하기 처리
        val navMenu = findViewById<NavigationView>(R.id.nav_view)
        navMenu.setNavigationItemSelectedListener {

            // Kotliln에는 switch문이 없다.
            Log.i(TAG,"누른게뭐지: $it")
            when(it.itemId) {
                // 요청중인 심부름 메뉴 클릭 시
                R.id.nav_errandReq -> {
                    Log.i(TAG,"[요청중인 심부름] 클릭하였음")
                    replaceFragment(reqErrandFragment)
                }
                // 심부름 지도 메뉴 클릭 시
                R.id.nav_errandMap -> {
                    Log.i(TAG,"[심부름 지도] 클릭하였음")
                    replaceFragment(mapFragment)
                }
            }
            val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            drawer.closeDrawer(GravityCompat.START)
            true
        }
    }
/*
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

 */




    // 프래그먼트를 갈아 끼우기
    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction() // 이 자체가 [객체 생성]이라고 볼 수 있다.
            .apply {
                replace(R.id.fragmentContainer, fragment) // fragmentContainer는 사전에 activity_main.xml에 준비해둔다.
                commit()
            }
    }
    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



}