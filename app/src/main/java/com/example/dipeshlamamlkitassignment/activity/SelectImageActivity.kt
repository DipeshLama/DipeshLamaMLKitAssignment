package com.example.dipeshlamamlkitassignment.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.example.dipeshlamamlkitassignment.R
import com.example.dipeshlamamlkitassignment.constants.RequestCodeConstants
import com.example.dipeshlamamlkitassignment.constants.TextConstants
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.android.synthetic.main.activity_live_image.*
import kotlinx.android.synthetic.main.activity_select_image.*
import java.io.IOException

class SelectImageActivity : AppCompatActivity(),View.OnClickListener {
    private var bitmap : Bitmap? = null
    private var picUri : Uri? = null
    private lateinit var detector : FaceDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image)
        initListener()
    }

    private fun initListener () {
        btnSelectFromGallery.setOnClickListener(this)
    }

    private fun openGallery () {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type= "image/*"
        startActivityForResult(intent,RequestCodeConstants.REQUEST_GALLERY_CODE)
    }

    override fun onClick(view: View?) {
        when(view){
            btnSelectFromGallery ->{
                openGallery()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RequestCodeConstants.REQUEST_GALLERY_CODE){
            picUri = data?.data
            if(picUri != null){
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver,picUri)
                    val image : InputImage? = InputImage.fromFilePath(this, picUri!!)
                    detectFace(image!!)
                }catch (e : IOException){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun detectFace (image : InputImage){
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()

        detector = FaceDetection.getClient(options)
        detector.process(image)
            .addOnSuccessListener { faces ->
                for (face in faces){
                    if(face.smilingProbability != null){
                        val smileProb = face.smilingProbability
                        txvSelectSmileProb.text = smileProb.toString()
                        if(smileProb!! > 0.6){
                            txvSelectIsSmiling.text = TextConstants.isSmiling
                        }else{
                            txvSelectIsSmiling.text = TextConstants.notSmiling
                        }
                    }
                    if(face.rightEyeOpenProbability != null){
                        val rightEyeOpenProb = face.rightEyeOpenProbability
                        txvSelectRightProb.text = rightEyeOpenProb.toString()
                        if(rightEyeOpenProb!! > 0.5){
                            txvSelectIsRightOpen.text = TextConstants.rightEyeOpen
                        }else{
                            txvSelectIsRightOpen.text = TextConstants.rightEyeClose
                        }
                    }
                    if(face.leftEyeOpenProbability != null){
                        val leftEyeOpenProb = face.leftEyeOpenProbability
                        txvSelectLeftProb.text = leftEyeOpenProb.toString()
                        if(leftEyeOpenProb!! > 0.5){
                            txvSelectIsLeftOpen.text = TextConstants.leftEyeOpen
                        }else{
                            txvSelectIsLeftOpen.text = TextConstants.leftEyeClose
                        }
                    }
                }

            }
    }
}