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
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
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

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var memberService: MemberService

    // 회원정보 - 로그인 액티비티가 화면에 올라올 때 필요할 수도 있는 정보
    private lateinit var isSavedId: String
    private lateinit var memEmail: String

//    private val MY_PERMISSIONS_REQUEST_LOCATION: Int = 1001 // location 권한에 고유번호를 부여하는 작업 즉, 1001번 권한은 location권한을 의미.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, OpenLoadingActivity::class.java)
        startActivity(intent)

        // 로그인 화면을 열기 전, 자동 로그인 회원인지를 먼저 확인한다.
        // SharedPreferences울 가져와 값 담기 - 최신화
        sharedPreferences = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val isAutoLogin: String? = sharedPreferences.getString("autoLogin", "false")
        isSavedId = sharedPreferences.getString("savedId", "false").toString()
        memEmail = sharedPreferences.getString("mem_email", "").toString()
        val memPw: String? = sharedPreferences.getString("mem_pw", "")

        // 자동 로그인 회원이라면
        if ("true".equals(isAutoLogin) && memPw != null && memEmail != null) {
            Log.i(TAG, "자동 로그인 회원입니다.")
            // Tomcat 서버로 연결해서 회원인지 체크받기
            checkIsMember(memEmail, memPw)
        }

        setContentView(R.layout.activity_login)

        // 아이디 저장 회원이라면
        if ("true".equals(isSavedId) && memEmail != null && !"".equals(memEmail)) {
            Log.i(TAG, "아이디 저장 회원입니다.")
            Log.i(TAG, memEmail)
            // 화면에 아이디를 띄워주세요
            findViewById<EditText>(R.id.et_id).text = Editable.Factory.getInstance().newEditable(memEmail)
            // 아이디 저장에 체크해주세요
            findViewById<CheckBox>(R.id.cb_saveId).isChecked = true
        }


    }

    // Tomcat 서버로 연결해서 회원인지 체크받기 - 이 요청은 LoginHandlerInterceptor에 의해 intercept 당해서 header를 처리받게 된다.
    private fun checkIsMember(memEmail: String, memPw: String) {
        Log.i(TAG, "checkIsMember 메소드 호출 성공")
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
            .enqueue(object : Callback<String> {
                // api 요청 성공시
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    
                    // SharedPreferences와 Editor 가져오기
                    sharedPreferences =
                        applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()

                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "Not Success!!")
                        return
                    }
                    // 결과 출력
                    Log.i(TAG, response.body().toString())
                    // 로그인 결과가 실패인 경우
                    if ("login fail".equals(response.body().toString())) {
                        Toast.makeText(applicationContext, "로그인 실패 ㅠ.ㅠ", Toast.LENGTH_SHORT).show()
                        // 아이디 저장 회원이라면 비밀번호만 지운다.
                        Log.i(TAG, "isSavedId?  $isSavedId")   // 아이디 저장 회원이 아닌 경우 false
                        if("false".equals(isSavedId) || isSavedId == null) editor.remove("mem_email")
                        editor.remove("mem_pw")
                        editor.commit()
                    }
                    // 로그인 결과가 성공인 경우 - mem_nickname을 반환받음
                    else {
                        Log.i(TAG, "로그인 성공, 메인액티비티로 이동합니다.")
                        // Tomcat 서버로부터 반환 받은 닉네임을 SharedPreferences에 저장하기
                        editor.putString("mem_nickname", response.body().toString())
                        editor.commit()
                        // 메인 액티비티로 이동 처리
                        var intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

                // api 요청 실패시
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }

            })
    }

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
        sharedPreferences = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // 자동로그인|아이디저장에 체크했니?
        val cbSaveId = findViewById<CheckBox>(R.id.cb_saveId)
        val cbAutoLogin = findViewById<CheckBox>(R.id.cb_autoLogin)
        if(cbSaveId.isChecked) {
            editor.putString("savedId","true")
            isSavedId = "true"
        }
        else {
            editor.remove("savedId")
            isSavedId = "false"
        }
        if(cbAutoLogin.isChecked) editor.putString("autoLogin","true")
        else editor.remove("autoLogin")
        Log.i(TAG, "cbSaveId: ${cbSaveId.isChecked}, cbAutoLogin: ${cbAutoLogin.isChecked}")


        // 회원 아이디와 비번 비교하는 작업 - insert_here
        // SharedPreferences에 사용자가 입력한 id 와 pw를 넣기


        val memEmail: String = findViewById<EditText>(R.id.et_id).text.toString()
        val memPw: String = findViewById<EditText>(R.id.et_pw).text.toString()
        Log.i(TAG, "입력된 아이디: $memEmail")
        Log.i(TAG, "입력된 비밀번호: $memPw")

        editor.putString("mem_email", memEmail)
        editor.putString("mem_pw", memPw)
        editor.commit() // 꼭 해줘야 됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

//        // 클라이언트 객체 생성 후 인터셉터 설정하기
//        val client = OkHttpClient()
//        Log.i(TAG, "${client.interceptors()}")
//        client.interceptors()[0] = LoginHandleInterceptor(applicationContext)
//            Log.i(TAG, "${client.interceptors()[0]}")
////            .add(0, LoginHandleInterceptor(applicationContext))

//        val gson: Gson = GsonBuilder().setLenient().create()
        checkIsMember(memEmail, memPw)

    }
}