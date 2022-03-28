package com.example.dipeshlamamlkitassignment.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.dipeshlamamlkitassignment.R
import com.example.dipeshlamamlkitassignment.constants.ErrorConstants
import com.example.dipeshlamamlkitassignment.detector.FaceDetectorProcessor
import com.example.dipeshlamamlkitassignment.vision.CameraSource
import com.example.dipeshlamamlkitassignment.vision.CameraSourcePreview
import com.example.dipeshlamamlkitassignment.vision.GraphicOverlay
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.android.synthetic.main.activity_live_preview.*
import java.io.IOException

class LivePreviewActivity : AppCompatActivity(),CompoundButton.OnCheckedChangeListener {
    private var cameraSource: CameraSource? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_preview)
        createCameraSource()
        initListener()
    }

    fun initListener(){
        facing_switch.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource?.setFacing(CameraSource.CAMERA_FACING_FRONT)
            } else {
                cameraSource?.setFacing(CameraSource.CAMERA_FACING_BACK)
            }
        }
        livePreview?.stop()
        startCameraSource()
    }

    private fun createCameraSource(){
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.1f)
            .enableTracking()
            .build()

        if(cameraSource == null){
            cameraSource = CameraSource(this, graphicOverLay)
        }
        try{
            cameraSource!!.setMachineLearningFrameProcessor(
                FaceDetectorProcessor(this, options)
            )

        }catch (e: Exception){
            Toast.makeText(applicationContext, ErrorConstants.camerSourceError+ e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun startCameraSource() {
        if (cameraSource != null) {
            try {
                livePreview!!.start(cameraSource, graphicOverLay)
            } catch (e: IOException) {
                cameraSource!!.release()
                cameraSource = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        createCameraSource()
        startCameraSource()
    }
}