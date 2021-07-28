package com.example.android_nds

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.android_nds.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private var chatBinding: ActivityChatBinding? = null
    private val binding get()= chatBinding!!

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
    }
}