package com.example.campsiteapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.asImageBitmap
import com.example.campsiteapp.controller.QrCodeService

class QrScanScreen {

    /**
     * This class shows a QR code to show the current selected campsite details
     */

    @Composable
    fun QrScan(
        campsiteId: String
    ) {

        /**
         * This function uses QrCodeGenerator to generate and show a QR code on the screen
         *
         * @param campsiteId is passed to this function to create a Qr code of it's details
         */

        val qrCodeService = QrCodeService()

        val qrBitmap = remember {
            qrCodeService.generateQRCode(campsiteId)
        }

        Image(
            bitmap = qrBitmap.asImageBitmap(),
            contentDescription = "QR Code",
            modifier = Modifier.size(250.dp)
        )
    }

}