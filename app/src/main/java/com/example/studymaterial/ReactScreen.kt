package com.example.studymaterial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReactScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_react_screen)

        val textViewLargeText: TextView = findViewById(R.id.textViewLargeText)
        // Example: Setting a large amount of text
        val largeText = getString(R.string.react) // Assuming you have a string resource named large_text
        textViewLargeText.text = largeText



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }

        findViewById<Button>(R.id.downloadPdfButton).setOnClickListener {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            val textPaint = TextPaint().apply {
                // Configure your paint (text size, color, etc.) as needed
                textSize = 16f
            }
            val canvas = page.canvas

//            val paint = Paint()
            val text = getString(R.string.large_text_java)
//            canvas.drawText(text, 5f, 20f, paint)

            val textWidth = pageInfo.pageWidth - (2 * 10)  // Calculate text width with some margin
            val staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, textWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build()

// Draw the StaticLayout on canvas
            canvas.save()
            canvas.translate(10f, 20f) // Adjust for margins
            staticLayout.draw(canvas)
            canvas.restore()

            pdfDocument.finishPage(page)

            val filePath = File(getExternalFilesDir(null), "react.pdf")
            try {
                pdfDocument.writeTo(FileOutputStream(filePath))
                val currentTime = System.currentTimeMillis()
                val formattedTime = formatTime(currentTime)
                val userEmail = FirebaseAuth.getInstance().currentUser?.email
                saveDataToDatabase(filePath.name, formattedTime, userEmail)
                Toast.makeText(this, "PDF downloaded!", Toast.LENGTH_SHORT).show()

                // Open the PDF file
                val fileUri: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.studymaterial"+".provider",
                    filePath
                )

                val openIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(fileUri, "application/pdf")
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                startActivity(openIntent)

            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error downloading PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                pdfDocument.close()
            }
        }
    }

    fun formatTime(currentTime: Long): String {
        val sdf = SimpleDateFormat("MMMM dd yyyy hh:mm a", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTime
        return sdf.format(calendar.time)
    }

    fun saveDataToDatabase(pdfFilename: String, currentTime: String, userEmail: String?) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://studymaterial-97b09-default-rtdb.firebaseio.com/")
        val reference: DatabaseReference = database.reference

        // Create a unique key for the data entry
        val key = reference.child("downloads").push().key ?: ""

        // Create a HashMap to hold the data
        val data = HashMap<String, Any>()
        data["pdfFilename"] = pdfFilename
        data["currentTime"] = currentTime
        userEmail?.let { data["userEmail"] = it }
        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()


        // Save the data to the database under the "downloads" node with the unique key
        reference.child("downloads").child(key).setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Data saved succesfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }


}
