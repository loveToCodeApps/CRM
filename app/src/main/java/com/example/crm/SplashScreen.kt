package com.example.crm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.crm.databinding.ActivitySplashScreenBinding


class SplashScreen : AppCompatActivity() {
    lateinit var binding:ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val zoom_anim = AnimationUtils.loadAnimation(this,R.anim.zoom_in)
//        val spin_anim = AnimationUtils.loadAnimation(this,R.anim.rotate)
//        val title_anim = AnimationUtils.loadAnimation(this,R.anim.left_to_center)
//        binding.logo.animation=logoAnim
        binding.logo.animation=zoom_anim

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3200)
    }
}




