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

class MainActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var btnRealImage : AppCompatButton
    private lateinit var btnSelectImage : AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnRealImage = findViewById(R.id.btnLiveImage)
        btnSelectImage = findViewById(R.id.btnSelectImage)

        if (!hasPermissions()){
            requestPermission()
        }

        initListener()
    }

    private fun initListener (){
        btnRealImage.setOnClickListener(this)
        btnSelectImage.setOnClickListener(this)
    }

    private fun hasPermissions() : Boolean{
        var hasPermission = true
        for (permission in permissions){
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED ){
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

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions, 1)
    }

    override fun onClick(view: View?) {
        when (view){
            btnRealImage -> {
                startActivity(Intent(this, LiveImageActivity::class.java ))
            }
            btnSelectImage ->{
                startActivity(Intent(this, SelectImageActivity::class.java ))
            }
        }
    }
}