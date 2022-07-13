package com.example.dipeshlamamlkitassignment.activity

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentManager
import com.example.dipeshlamamlkitassignment.fragment.BottomSheetFragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class ImageAnalyzer(
    val fragmentManager: FragmentManager,
    val dialog: BottomSheetFragment,
) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        scanBarCode(image)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun scanBarCode(image: ImageProxy) {
        val image1 = image.image

        val inputImage =
            image1?.let { InputImage.fromMediaImage(it, image.imageInfo.rotationDegrees) }

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            ).build()

        val scanner = BarcodeScanning.getClient(options)
        val result = inputImage?.let {
            scanner.process(it)
                .addOnSuccessListener { barcodes ->
                    renderBarCodes(barcodes)
                }

                .addOnFailureListener {

                }
                .addOnCompleteListener {
                    image.close()
                }

        }
    }

    private fun renderBarCodes(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            val bounds = barcode.boundingBox
            val corners = barcode.cornerPoints

            val rawValue = barcode.rawValue
            // See API reference for complete list of supported types
            when (barcode.valueType) {
                Barcode.TYPE_WIFI -> {
                    val ssid = barcode.wifi!!.ssid
                    val password = barcode.wifi!!.password
                    val type = barcode.wifi!!.encryptionType
                }
                Barcode.TYPE_URL -> {
                    if (!dialog.isAdded) {
                        dialog.show(fragmentManager, "")
                    }
                    barcode.url?.url?.let {
                        dialog.fetchUrl(it)
                    }
                    val title = barcode.url!!.title
                    val url = barcode.url!!.url

                }
            }
        }
    }
}