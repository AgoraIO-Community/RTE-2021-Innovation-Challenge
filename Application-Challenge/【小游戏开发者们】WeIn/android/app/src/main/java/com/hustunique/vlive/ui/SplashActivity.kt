package com.hustunique.vlive.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hustunique.vlive.util.startActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity<VLiveHostActivity>()
        finish()
    }
}