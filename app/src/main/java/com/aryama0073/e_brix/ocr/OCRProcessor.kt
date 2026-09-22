package com.aryama0073.e_brix.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

fun recognizeText(bitmap: Bitmap, onResult: (String) -> Unit) {
    val image = InputImage.fromBitmap(bitmap, 0)
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            onResult(visionText.text)
        }
        .addOnFailureListener {
            onResult("Gagal membaca teks")
        }
}