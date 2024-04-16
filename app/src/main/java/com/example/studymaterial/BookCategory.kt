package com.example.studymaterial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TableRow

class BookCategory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_category)

        val gotoPython = findViewById<TableRow>(R.id.python)
        val gotoflutter = findViewById<TableRow>(R.id.Flutter)
        val gotoReact = findViewById<TableRow>(R.id.React)
        val gotoPhp = findViewById<TableRow>(R.id.php)
        val gotoJava = findViewById<TableRow>(R.id.java)
        val gotoProfile = findViewById<ImageButton>(R.id.imageButton)


        gotoPython.setOnClickListener{
            val intent = Intent(applicationContext, FirstBookScreen::class.java)
            startActivity(intent)
        }
        gotoflutter.setOnClickListener{
            val intent = Intent(applicationContext, Flutter::class.java)
            startActivity(intent)
        }
        gotoReact.setOnClickListener{
            val intent = Intent(applicationContext, ReactScreen::class.java)
            startActivity(intent)
        }
        gotoPhp.setOnClickListener{
            val intent = Intent(applicationContext, Phpcreen::class.java)
            startActivity(intent)
        }
        gotoJava.setOnClickListener{
            val intent = Intent(applicationContext, JavaScreen::class.java)
            startActivity(intent)
        }
        gotoProfile.setOnClickListener{
            val intent = Intent(applicationContext, ProfileScreen::class.java)
            startActivity(intent)
        }




    }


}