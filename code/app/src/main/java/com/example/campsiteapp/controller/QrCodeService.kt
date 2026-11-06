package com.example.campsiteapp.controller
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

class QrCodeService {



    fun generateQRCode(text: String): Bitmap {

        val writer = QRCodeWriter()

        val bitMatrix = writer.encode(
            text,
            BarcodeFormat.QR_CODE,
            512,
            512
        )

        val bitmap = createBitmap(512, 512, Bitmap.Config.RGB_565)

        for (x in 0 until 512) {
            for (y in 0 until 512) {
                bitmap[x, y] = if (bitMatrix[x, y]) android.graphics.Color.BLACK
                else android.graphics.Color.WHITE
            }
        }

        return bitmap
    }

}