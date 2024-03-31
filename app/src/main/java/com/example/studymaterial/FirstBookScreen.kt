package com.example.studymaterial

import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FirstBookScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_book_screen)


        val textViewLargeText: TextView = findViewById(R.id.textViewLargeText)
        // Example: Setting a large amount of text
        val largeText = getString(R.string.large_text) // Assuming you have a string resource named large_text
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
            val text = getString(R.string.large_text)
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

            val filePath = File(getExternalFilesDir(null), "python.pdf")
            try {
                pdfDocument.writeTo(FileOutputStream(filePath))
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
}

