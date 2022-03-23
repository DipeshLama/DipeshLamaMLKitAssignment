package com.example.dipeshlamamlkitassignment.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dipeshlamamlkitassignment.R
import com.example.dipeshlamamlkitassignment.constants.RequestCodeConstants
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.android.synthetic.main.activity_live_image.*
import java.lang.Exception


class LiveImageActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var image : InputImage
    private lateinit var detector : FaceDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_image)
        initListener()
    }

    private fun initListener(){
        btnClickImage.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btnClickImage ->{
                openCamera()
            }
        }
    }

    private fun openCamera (){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, RequestCodeConstants.REQUEST_IMAGE_CAPTURE);
        }else{
            Toast.makeText(this, "Something is wrong.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RequestCodeConstants.REQUEST_IMAGE_CAPTURE && resultCode === Activity.RESULT_OK) {
            val extra: Bundle = data!!.extras!!
            val bitmap = extra["data"] as Bitmap?
            detectFace(bitmap!!)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun detectFace (bitmap : Bitmap){
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()

        try {
            image = InputImage.fromBitmap(bitmap,0)
            detector = FaceDetection.getClient(options)
        }catch (e: Exception){
            e.printStackTrace()
        }

        detector.process(image)
            .addOnSuccessListener { faces ->
                for (face in faces) {

                    if(face.smilingProbability != null){
                        val smileProb = face.smilingProbability
                        txvLiveSmileProb.text = smileProb.toString()
                        if(smileProb!! > 0.6){
                            txvLiveIsSmiling.text = "The person is smiling"
                        }else{
                            txvLiveIsSmiling.text = "The person is not smiling"
                        }
                    }

                    if(face.rightEyeOpenProbability != null){
                        val rightEyeOpenProb = face.rightEyeOpenProbability
                        txvLiveRightProb.text = rightEyeOpenProb.toString()
                        if(rightEyeOpenProb!! > 0.5){
                            txvLiveIsRightOpen.text = "Right eye is open "
                        }else{
                            txvLiveIsRightOpen.text = "Right eye is close"
                        }
                    }

                    if(face.leftEyeOpenProbability != null){
                        val leftEyeOpenProb = face.leftEyeOpenProbability
                        txvLiveLeftProb.text = leftEyeOpenProb.toString()
                        if(leftEyeOpenProb!! > 0.5){
                            txvLiveIsLeftOpen.text = "Left eye is open "
                        }else{
                            txvLiveIsLeftOpen.text = "Left eye is close"
                        }
                    }
                }
            }
    }
}