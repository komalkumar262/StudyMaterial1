package com.example.studymaterial
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.doAfterTextChanged
import com.google.firebase.auth.FirebaseUser

class HomeScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

//        go to login screen
        val gotoLoginScreen = findViewById<Button>(R.id.goTOLogin)
        gotoLoginScreen.setOnClickListener{
            val intent2 = Intent(this,LoginScreen::class.java)
            startActivity(intent2)
        }

        // Initialize the FirebaseAuth instance
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
//            // User is already logged in
//            Toast.makeText(applicationContext, "You are logged in", Toast.LENGTH_SHORT).show()
            // Navigate to ProfileActivity
            navigateToProfile()
            Toast.makeText(applicationContext, "going to profile page", Toast.LENGTH_SHORT).show()
        }

        // Reference to the email and password EditText fields
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)

        // Reference to the submit button
        val submitButton = findViewById<Button>(R.id.submitButton)

        // Set an OnClickListener on the submit button

        submitButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            submitButton.isEnabled = false
            passwordEditText.doAfterTextChanged {
                // Enable the submit button if the password length is 6 or more characters
                submitButton.isEnabled = it.toString().length >= 6
            }



            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Use Firebase Auth to create a new user
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Register", "createUserWithEmail:success")
                            val user = auth.currentUser
                            // You can now redirect the user to another activity or update your UI
                            val goToprofilePage = findViewById<Button>(R.id.submitButton)
                            goToprofilePage.setOnClickListener {
                                val intent = Intent(this, ProfileScreen::class.java)
                                startActivity(intent)
                            }


                        } else {
                            print(email)
                            print(password)

                            // If sign in fails, display a message to the user.
                            Log.w("Register", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                            // Update your UI to show the failure

                        }
                    }
            } else {
                Toast.makeText(baseContext, "Please fill in both fields.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun navigateToProfile() {
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
        finish()
        // Optional: if you don't want the user to come back to this activity on pressing back button
    }

//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            Toast.makeText(applicationContext, "already logged, profile page", Toast.LENGTH_SHORT).show()
//            navigateToNextActivity()
//        }
//    }



}



