package com.example.android_nds

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    // 지도 선언
    private lateinit var mMap: GoogleMap
    // 현재 위치를 담는다
    private lateinit var currentLoc: Location
    private val MY_PERMISSIONS_REQUEST_LOCATION: Int = 1001 // location 권한에 고유번호를 부여하는 작업 즉, 1001번 권한은 location권한을 의미.

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG,"여기여기")
        // https://androidnds-9ac2f-default-rtdb.asia-southeast1.firebasedatabase.app/



        // 프래그먼트의 초기화
        val reqErrandFragment = ReqErrandFragment()
        val allListItemActivity = AllListItemActivity()
//        val ndsMapFragment = NDSMapFragment()
        // 지도 설정
        val mapMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
        mapMapFragment.getMapAsync(this)
//        replaceFragment(mapMapFragment)
        // Navigation Menu를 클릭시 프래그먼트 교체 또는 액티비티 이동하기 처리
        val navMenu = findViewById<NavigationView>(R.id.nav_view)
        navMenu.setNavigationItemSelectedListener {

            // Kotlin에는 switch문이 없다.
            Log.i(TAG,"누른게뭐지: $it")
            when(it.itemId) {
                // 요청중인 심부름 메뉴 클릭 시
                R.id.nav_errandReq -> {
                    Log.i(TAG,"[요청중인 심부름] 클릭하였음")
                    // 현재 프래그먼트가 요청중인 심부름 프래그 먼트가 아닐때만 바꿔주세요
                    if(currentFragment != reqErrandFragment) replaceFragment(reqErrandFragment)
                }
                // 심부름 지도 메뉴 클릭 시
                R.id.nav_errandMap -> {
                    Log.i(TAG,"[심부름 지도] 클릭하였음")
                    // 테스트
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.478768, 126.878879)))
                    // 현재 위치 갱신하기
                    // map이 생성되지 않았다면, map부터 생성해주기
                    if(mMap != null){
                        getLocation()
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(currentLoc.latitude, currentLoc.longitude)))
                        plusMarker(mMap, currentLoc)
                    }
//                    mMap.
//                    replaceFragment(ndsMapFragment)
//                    replaceFragment(mapMapFragment)
                    if(currentFragment != null) removeFragment()
//                    Log.i(TAG,"프래그먼트 제거 완료")
//
//                    replaceFragment()
                }
                // 중고거래 목록 보기 메뉴 클릭 시
                R.id.all_item_list -> {
                    Log.i(TAG,"[중고거래 목록 보기] 클릭하였음")
                    val intent = Intent(this, AllListItemActivity::class.java)
                    startActivity(intent)
                }
                // 로그아웃 클릭 시
                R.id.nav_logout -> {
                    Log.i(TAG,"로그아웃 클릭하였음")
                    logout()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            drawer.closeDrawer(GravityCompat.START)
            true
        }
        requestLocPermission()
        getLocation()
    }

    // 로그아웃 버튼 클릭 시
    fun logout(){
        Log.i(TAG,"logout 메소드 호출 성공")
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        // ============================================ 출력해보기 시작
        Log.i(TAG,"MainActivity::isSavedId? ${sharedPreferences.getString("savedId", "false")}")
        Log.i(TAG,"MainActivity::isAutoLogin? ${sharedPreferences.getString("autoLogin", "false")}")
        Log.i(TAG,"MainActivity::memEmail? ${sharedPreferences.getString("mem_email", "")}")
        Log.i(TAG,"MainActivity::memPw? ${sharedPreferences.getString("mem_pw", "")}")
        // ============================================ 출력해보기 끝
        // 자동 로그인은 해제함
        editor.remove("autoLogin")
        // 아이디 저장 회원이 아닌 경우와 아이디 저장 회원인 경우를 나눠서 처리
        val isSavedId: String = sharedPreferences.getString("savedId", "false").toString()
        if(isSavedId == null || "false".equals(isSavedId)) editor.remove("mem_email")
        editor.remove("mem_pw")
        editor.commit() // 커밋 꼭 해줘야 함!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
    
    
    
    // 위치접근 권한 요청하기
    private fun requestLocPermission(){
        var locPermission: Int = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )   // Manifest에 이 권한이 등록되어 있지 않으면 오류를 내는 것이 아니라 권한이 허용되지 않음(-1)으로 표시될 뿐이다. 이것때문에 삽질하지 않도록 주의하자!!!!★★★★★★
        Log.i(TAG,"여기여기여기"+locPermission)
        if (locPermission != PackageManager.PERMISSION_GRANTED) {  // [위치 접근 권한]이 [허용]된 상태가 아니라면
            ActivityCompat.requestPermissions(  // [위치 접근 권한]을 [허용]해 줄 것을 요청한다. - permission request
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
            return
        }
    }

    /* 권한 요청 결과 - 콜백 */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        Log.i(TAG, "resultCode => ${requestCode}")    // 1001 출력됨 - 지금 보게 되는 결과가 위치권한에 관한 것임을 의미
//        Log.i(TAG, "${grantResults[0]}")    // -1 출력됨
//        Log.i(TAG, "grantResults => ${grantResults}")
//        Log.i(TAG, "permissions[0] => ${permissions[0]}")    // android.permission.ACCESS_FINE_LOCATION 출력됨
//        Log.i(TAG, "${grantResults[1]}")    // ArrayIndexOutOfBoundsException
        /* 오류 세부사항 - ${grantResults[1]}
         * java.lang.RuntimeException: Failure delivering result ResultInfo{who=@android:requestPermissions:, request=1001, result=-1, data=Intent { act=android.content.pm.action.REQUEST_PERMISSIONS (has extras) }} to activity {com.example.android_nds/com.example.android_nds.LoginActivity}: java.lang.ArrayIndexOutOfBoundsException: length=1; index=1
         */
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {   // requestCode가 REQUEST_CODE_PERMISSIONS이면,,,
                // all >> grantResults라는 int 배열에 담긴 값들(즉, 권한들의 상태)이(it으로 받아짐) [모두] [허용] 상태라면 true를 반환함.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(this, "위치 접근 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "위치 접근 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }

                Log.i(TAG, ""+ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ))

            }
            // 추가로 필요한 권한들이 있다면 이쪽에 분기할 수 있겠다.
        }
    }

    /* 위치가져오기 */
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val task: Task<Location> = fusedLocationClient.lastLocation
        lateinit var currentLocation: Location
        task.addOnSuccessListener { location ->
            if (location != null) currentLocation = location
            Toast.makeText(
                applicationContext, currentLocation.latitude.toString() + "," +
                        currentLocation.longitude, Toast.LENGTH_LONG
            ).show()
            currentLoc = location
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(currentLoc.latitude, currentLoc.longitude)))
        }
    }

    // 프래그먼트를 갈아 끼우기
    private fun replaceFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction() // 이 자체가 [객체 생성]이라고 볼 수 있다.
            .apply {
                add(R.id.fragmentContainer, fragment) // fragmentContainer는 사전에 activity_main.xml에 준비해둔다.
//                replace(R.id.willbereplaced, fragment)
                currentFragment = fragment // 현재 화면에 띄워질 프래그먼트 담기
                Log.i(TAG, "$currentFragment 를 붙여요")
                commit()
            }
    }
    private fun removeFragment() {
        supportFragmentManager.beginTransaction() // 이 자체가 [객체 생성]이라고 볼 수 있다.
            .apply {
                Log.i(TAG, "$currentFragment 를 뗍니다~~~")
                currentFragment?.let { remove(it) } // fragmentContainer는 사전에 activity_main.xml에 준비해둔다.
                currentFragment = null
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

    // 지도가 준비되었을 때 - 콜백메소드
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val currentLocLatLng = LatLng(37.478665, 126.878204)
//        val currentLocLatLng = LatLng(currentLoc.latitude, currentLoc.longitude)
        mMap.addMarker(
            MarkerOptions()
            .position(currentLocLatLng))
        mMap.setMinZoomPreference(15.0f)
    }
    
    private fun plusMarker(googleMap: GoogleMap, currentLog: Location){
        // addMarker는 Marker를 return한다.
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(currentLoc.latitude, currentLoc.longitude))
                .title("나의 위치")
        )
    }

    companion object {
        private const val TAG = "mymymy"
    }

}