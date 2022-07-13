package com.example.dipeshlamamlkitassignment.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dipeshlamamlkitassignment.R
import com.example.dipeshlamamlkitassignment.constants.BundleConstants
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.android.synthetic.main.activity_object_detection.*

class ObjectDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_detection)
        btnCamOpenObjDetection.setOnClickListener {
            openCamera()
        }
        btnImageLabeling.setOnClickListener {
            imageLabeling()
        }
    }

    private fun imageLabeling() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcher.launch(intent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    val extra = result.data?.extras
                    val bitmap = extra?.get(BundleConstants.data) as Bitmap?
                    if (bitmap != null) {
                        detectImageLabeling(bitmap)
                    }
                }
                RESULT_CANCELED -> return@registerForActivityResult
            }
        }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    val extra = result.data?.extras
                    val bitmap = extra?.get(BundleConstants.data) as Bitmap?
                    if (bitmap != null) {
                        detectImage(bitmap)
                    }
                }
                RESULT_CANCELED -> {
                    return@registerForActivityResult
                }
            }
        }

    private fun detectImageLabeling(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        labeler.process(image)
            .addOnSuccessListener {
                processImageLabeling(it)

            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun processImageLabeling(labels: List<ImageLabel>) {
        txvObjectText.text = labels[0].text
    }


    private fun detectImage(bitmap: Bitmap) {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()
        val detector = ObjectDetection.getClient(options)
        val image = InputImage.fromBitmap(bitmap, 0)

        detector.process(image)
            .addOnSuccessListener { objects ->
                processImage(objects)

            }
            .addOnFailureListener {
                Toast.makeText(this,
                    it.message,
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun processImage(objects: MutableList<DetectedObject>) {
        if (objects.isNotEmpty()) {
            val builder = StringBuilder()
            for (ob in objects) {
                if (ob.labels.isNotEmpty()) {
                    val label = ob.labels[0].text
                    builder.append(label).append("\n")
                } else {
                    Toast.makeText(this, "Could not detect image", Toast.LENGTH_SHORT).show()
                }
            }
            txvObjectText.text = builder.toString()
        }
    }
}