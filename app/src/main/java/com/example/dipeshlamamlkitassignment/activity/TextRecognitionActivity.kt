package com.example.dipeshlamamlkitassignment.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dipeshlamamlkitassignment.R
import com.example.dipeshlamamlkitassignment.constants.BundleConstants
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.android.synthetic.main.activity_text_recognition.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.example.dipeshlamamlkitassignment.constants.ErrorConstants as ErrorConstants1

class TextRecognitionActivity : AppCompatActivity() {

    private lateinit var imageBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognition)

        btnCamera.setOnClickListener {
            openCamera()
        }

        btnGallery.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickLauncher.launch(intent)

    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }

    private val imagePickLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                Toast.makeText(this,
                    ErrorConstants1.errorMessage,
                    Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
            val picUri = result.data?.data
            if (picUri != null) {
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, picUri)
                    detectImage(imageBitmap)

                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
            imvTextRecognition.setImageURI(picUri)
        }

    private val resultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                Toast.makeText(this,
                    ErrorConstants1.errorMessage,
                    Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }
            val extra = result.data?.extras
            val uri = result.data?.data
            val bitmap = extra?.get(BundleConstants.data) as Bitmap?
            if (bitmap != null) {
                detectImage(bitmap)
            }
            imvTextRecognition.setImageURI(uri)
        }

    private fun detectImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener {
                processImage(it)
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun processImage(result: Text) {
        var blockText = ""
        val resultText = result.text
        for (block in result.textBlocks) {
//            blockText += "\n${block.text}"
            val blockCornerPoints = block.cornerPoints
            val blockFrame = block.boundingBox
            for (line in block.lines) {
                val lineText = line.text
                val lineCornerPoints = line.cornerPoints
                val lineFrame = line.boundingBox
                for (element in line.elements) {
                    val elementText = element.text
                    val elementCornerPoints = element.cornerPoints
                    val elementFrame = element.boundingBox
                }
            }
        }
        txvText.text = resultText
    }
}