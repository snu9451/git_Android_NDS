package com.example.android_nds

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.android_nds.api.MemberService
import com.example.android_nds.interceptor.LoginHandleInterceptor
import com.example.android_nds.model.Items
import com.example.android_nds.model.MemberVO
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Member


class LoginActivity : AppCompatActivity() {

    val TAG: String = "mymymy"
    private lateinit var memberService: MemberService

//    private val MY_PERMISSIONS_REQUEST_LOCATION: Int = 1001 // location 권한에 고유번호를 부여하는 작업 즉, 1001번 권한은 location권한을 의미.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        var locPermission: Int = ActivityCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )   // Manifest에 이 권한이 등록되어 있지 않으면 오류를 내는 것이 아니라 권한이 허용되지 않음(-1)으로 표시될 뿐이다. 이것때문에 삽질하지 않도록 주의하자!!!!★★★★★★
//        Log.i(TAG,"여기여기여기"+locPermission)
//        if (locPermission != PackageManager.PERMISSION_GRANTED) {  // [위치 접근 권한]이 [허용]된 상태가 아니라면
//            ActivityCompat.requestPermissions(  // [위치 접근 권한]을 [허용]해 줄 것을 요청한다. - permission request
//                this,
//                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
//                MY_PERMISSIONS_REQUEST_LOCATION
//            )
//            return
//        }
//        getLocation()
    }

//    /* 위치가져오기 */
//    @SuppressLint("MissingPermission")
//    private fun getLocation() {
//        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        val task: Task<Location> = fusedLocationClient.lastLocation
//        lateinit var currentLocation:Location
//        task.addOnSuccessListener { location ->
//            if (location != null) currentLocation = location
//            Toast.makeText(
//                applicationContext, currentLocation.latitude.toString() + "," +
//                        currentLocation.longitude, Toast.LENGTH_LONG
//            ).show()
//        }
//    }




//    /* 권한 요청 결과 */
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
////        Log.i(TAG, "resultCode => ${requestCode}")    // 1001 출력됨 - 지금 보게 되는 결과가 위치권한에 관한 것임을 의미
////        Log.i(TAG, "${grantResults[0]}")    // -1 출력됨
////        Log.i(TAG, "grantResults => ${grantResults}")
////        Log.i(TAG, "permissions[0] => ${permissions[0]}")    // android.permission.ACCESS_FINE_LOCATION 출력됨
////        Log.i(TAG, "${grantResults[1]}")    // ArrayIndexOutOfBoundsException
//        /* 오류 세부사항 - ${grantResults[1]}
//         * java.lang.RuntimeException: Failure delivering result ResultInfo{who=@android:requestPermissions:, request=1001, result=-1, data=Intent { act=android.content.pm.action.REQUEST_PERMISSIONS (has extras) }} to activity {com.example.android_nds/com.example.android_nds.LoginActivity}: java.lang.ArrayIndexOutOfBoundsException: length=1; index=1
//         */
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_LOCATION -> {   // requestCode가 REQUEST_CODE_PERMISSIONS이면,,,
//                // all >> grantResults라는 int 배열에 담긴 값들(즉, 권한들의 상태)이(it으로 받아짐) [모두] [허용] 상태라면 true를 반환함.
//                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    Toast.makeText(this, "위치 접근 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "위치 접근 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
//                }
//
//                Log.i(TAG, ""+ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ))
//
//            }
//            // 추가로 필요한 권한들이 있다면 이쪽에 분기할 수 있겠다.
//        }
//    }

    // 회원가입 버튼 눌렀을 때의 이벤트
    // private으로 하면 찾을 수 없음
    fun join(view: View) {
        Log.i(TAG, "[회원가입] 버튼 클릭 완료 - 액티비티 전환")
        var intent = Intent(this, JoinActivity::class.java)
        startActivity(intent)
    }

    // 로그인 버튼 눌렀을 때의 이벤트
    fun login(view: View) {
        Log.i(TAG, "[로그인] 버튼 클릭 완료 - 액티비티 전환")
        // 회원 아이디와 비번 비교하는 작업 - insert_here
        // SharedPreferences에 사용자가 입력한 id 와 pw를 넣기
        val memEmail: String = findViewById<EditText>(R.id.et_id).text.toString()
        val memPw: String = findViewById<EditText>(R.id.et_pw).text.toString()
        Log.i(TAG, "입력된 아이디: $memEmail")
        Log.i(TAG, "입력된 비밀번호: $memPw")
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("mem_email", memEmail)
        editor.putString("mem_pw", memPw)
        editor.commit()

//        // 클라이언트 객체 생성 후 인터셉터 설정하기
//        val client = OkHttpClient()
//        Log.i(TAG, "${client.interceptors()}")
//        client.interceptors()[0] = LoginHandleInterceptor(applicationContext)
//            Log.i(TAG, "${client.interceptors()[0]}")
////            .add(0, LoginHandleInterceptor(applicationContext))

//        val gson: Gson = GsonBuilder().setLenient().create()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(LoginHandleInterceptor(applicationContext)).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.ip_num))
            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        memberService = retrofit.create(MemberService::class.java)
//        val member = MemberVO(memEmail, memPw)
//        memberService.selectIsMember(member)
        memberService.selectIsMember(memEmail, memPw)
            .enqueue(object: Callback<String> {
            //api 요청 성공시
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful.not()){
                    Log.e(TAG,"Not Success!!")
                    return
                }
                // 결과 출력
                Log.i(TAG, response.body().toString())
                // 로그인 결과가 성공인 경우
                if("login success".equals(response.body().toString())){
                    Log.i(TAG,"로그인 성공, 메인액티비티로 이동합니다.")
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
                // 로그인 결과가 실패인 경우
                else {
                    Toast.makeText(applicationContext, "로그인 실패 ㅠ.ㅠ", Toast.LENGTH_SHORT).show()
                }
            }
            //api 요청 실패시
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG,t.toString())
            }

        })
    }
}