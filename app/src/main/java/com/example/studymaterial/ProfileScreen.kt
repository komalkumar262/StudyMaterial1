package com.example.studymaterial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class ProfileScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Check if user is logged in
        val userEmailTextView: TextView = findViewById(R.id.userEmailTextView)
        val firstLetterTextView: TextView = findViewById(R.id.textView4)


        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is signed in
            val email = currentUser.email
            val firstLetter = currentUser.email!!.first().toUpperCase().toString()
            firstLetterTextView.text = firstLetter
            userEmailTextView.text = email ?: "No email found"
        } else {
            // No user is signed in
            userEmailTextView.text = "User not logged in"
            firstLetterTextView.text = ""
        }



    }



}