package com.example.studymaterial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        val gotoHomePage = findViewById<Button>(R.id.homepage)
        gotoHomePage.setOnClickListener{
            val intent = Intent(applicationContext, HomeScreen::class.java)
            startActivity(intent)
        }


    }
}

