package com.example.dipeshlamamlkitassignment.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.example.dipeshlamamlkitassignment.R

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnRealImage: AppCompatButton
    private lateinit var btnSelectImage: AppCompatButton
    private lateinit var btnLivePreview: AppCompatButton
    private lateinit var btnTextRecon: AppCompatButton
    private lateinit var btnBarcodeScanner: AppCompatButton
    private lateinit var btnObjectDetection: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRealImage = findViewById(R.id.btnLiveImage)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnLivePreview = findViewById(R.id.btnLivePreview)
        btnTextRecon = findViewById(R.id.btnTextRecognition)
        btnBarcodeScanner = findViewById(R.id.btnBarcode)
        btnObjectDetection = findViewById(R.id.btnObjectDetection)

        if (!hasPermissions()) {
            requestPermission()
        }

        initListener()
    }

    private fun initListener() {
        btnRealImage.setOnClickListener(this)
        btnSelectImage.setOnClickListener(this)
        btnLivePreview.setOnClickListener(this)
        btnTextRecon.setOnClickListener(this)
        btnBarcodeScanner.setOnClickListener(this)
        btnObjectDetection.setOnClickListener(this)
    }

    private fun hasPermissions(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(this,
                    permission) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
                break
            }
        }
        return hasPermission
    }

    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 1)
    }

    override fun onClick(view: View?) {
        when (view) {
            btnRealImage -> {
                startActivity(Intent(this, LiveImageActivity::class.java))
            }
            btnSelectImage -> {
                startActivity(Intent(this, SelectImageActivity::class.java))
            }
            btnLivePreview -> {
                startActivity(Intent(this, LivePreviewActivity::class.java))
            }
            btnTextRecon -> {
                startActivity(Intent(this, TextRecognitionActivity::class.java))
            }
            btnBarcodeScanner -> {
                startActivity(Intent(this, BarcodeScanningActivity::class.java))
            }
            btnObjectDetection -> {
                startActivity(Intent(this, ObjectDetectionActivity::class.java))
            }
        }
    }
}