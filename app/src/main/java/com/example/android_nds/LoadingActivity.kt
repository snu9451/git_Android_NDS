package com.example.android_nds

import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        // 트윈에니메이션 -Rotate 회전 xml의 코드를 로드해서 적용하기
        val anim: Animation = AnimationUtils.loadAnimation(
            applicationContext, // 현재 화면의 제어권자
            R.anim.rot_anim
        );    // 설정한 에니메이션 파일
        findViewById<ImageView>(R.id.loading_icon).startAnimation(anim);
        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({
            finish()    // 현재 액티비티 종료
        }, 1000)
    }
}