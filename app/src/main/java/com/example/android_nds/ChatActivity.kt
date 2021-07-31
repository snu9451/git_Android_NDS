package com.example.android_nds

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.android_nds.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private var chatBinding: ActivityChatBinding? = null
    private val binding get()= chatBinding!!

    // 웹뷰 및 웹뷰설정 선언
    private lateinit var webView: WebView
    private lateinit var webViewSet: WebSettings

    // 로그인한 회원의 정보 가져오기
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var memEmail: String

    companion object {
        private const val TAG = "mymymy"
    }

    
    // 이걸로 호출하니까 화면 안 나오고 하얀 화면만 나옴
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        Log.i(TAG, "채팅 액티비티 호출 성공")
////        chatBinding = ActivityChatBinding.inflate(layoutInflater)
////        setContentView(binding.root)
//        setContentView(R.layout.activity_chat)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "채팅 액티비티 호출 성공")
//        chatBinding = ActivityChatBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        setContentView(R.layout.activity_chat)
        setWebView()
    }

    private fun setWebView(){
        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient() // 클릭 시 새창이 뜨지 않도록 함
        webViewSet = webView.settings
        webViewSet.javaScriptEnabled = true
        webViewSet.setSupportMultipleWindows(false) // 새창 띄우기 허용 여부
        webViewSet.javaScriptCanOpenWindowsAutomatically = false // 자바스크립트 새창 띄우기(멀티뷰) 허용야뷰

        webViewSet.builtInZoomControls = false // 화면 줌 허용여부
        webViewSet.cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐시 허용 여부
        webViewSet.domStorageEnabled = true // 로컬 저장소 허용 여부
        webViewSet.useWideViewPort = true // 뷰 포트에 화면사이즈 맞추기
        webViewSet.loadWithOverviewMode = true; // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정

//        webView.loadUrl("https://naver.com")
        // 아이디(mem_email) 가져오기
        sharedPreferences = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        memEmail = sharedPreferences.getString("mem_email", "").toString()
        // 요청 url 헤더에 유저 이메일 넣어주기
        val headerMap = HashMap<String, String>()
        headerMap["mem_email"] = memEmail
        Log.i(TAG, "$headerMap")
        webView.loadUrl(getString(R.string.ip_num)+"/mainPage/chatroom.jsp?dest_email="+intent.getStringExtra("dest_email"), headerMap)
//        webView.loadUrl(getString(R.string.ip_num)+"/mainPage/main_page.nds", headerMap)


    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

}