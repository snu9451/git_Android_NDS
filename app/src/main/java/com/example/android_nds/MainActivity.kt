package com.example.android_nds

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    // 지도 선언
    private lateinit var mMap: GoogleMap

    companion object {
        private const val TAG = "MyMainActivityNDS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG,"여기여기")
        // https://androidnds-9ac2f-default-rtdb.asia-southeast1.firebasedatabase.app/

        // 프래그먼트의 초기화
        val reqErrandFragment = ReqErrandFragment()
        // 지도 설정
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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



    // 프래그먼트를 갈아 끼우기
    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction() // 이 자체가 [객체 생성]이라고 볼 수 있다.
            .apply {
                replace(R.id.fragmentContainer, fragment) // fragmentContainer는 사전에 activity_main.xml에 준비해둔다.
                commit()
            }
    }

    //  뒤로가기 버튼을 눌렀을 때
    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(37.478665, 126.878204)
        mMap.addMarker(
            MarkerOptions()
            .position(sydney)
            .title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setMinZoomPreference(15.0f)
    }


}