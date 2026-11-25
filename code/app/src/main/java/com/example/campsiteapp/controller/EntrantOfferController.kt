package com.example.campsiteapp.controller

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.campsiteapp.data.LotteryDatabase
import com.example.campsiteapp.data.Reservation
import com.example.campsiteapp.model.Lottery
import com.example.campsiteapp.model.ReservationDatabase
import com.example.campsiteapp.view.EntrantOfferScreen
import java.io.File

object EntrantOfferController {
    private val lotteryDB by lazy { LotteryDatabase() }
    private val reservationDB by lazy { ReservationDatabase() }

    fun toggleOfferScreen(context: Context, lottery: Lottery, hasOffer: Boolean) {
        val intent = Intent(context, EntrantOfferScreen::class.java).apply {
            putExtra("hasOffer", hasOffer)
            putExtra("lottery", lottery)
        }
        context.startActivity(intent)
    }

    /**
        Creates a reservation and updates it to the firebase reservation database. Returns the
        reservation that it created.
        Uses lottery.bookingStartDate, and lottery.bookingEndDate as the start and end date
     **/
    fun entrantAcceptOfferHandler(deviceId: String, lottery: Lottery): Reservation {
        var reservation: Reservation = Reservation(
            campsiteId = lottery.campsite.id,
            entrantId = deviceId,
            startDate = lottery.bookingStartDate,
            endDate = lottery.bookingEndDate,
            status = "accepted",
            campsite = lottery.campsite
        )

        reservationDB.addReservation(reservation)

        return reservation
    }

    /**
        Removes the user from the lottery waitlist, and calls
        the PoolingSystem to draw one more user
     */
    fun entrantRejectOfferHandler(deviceId: String, lottery: Lottery){
        lottery.waitlist.remove(deviceId)
        lotteryDB.updateLottery(lottery)
    }

    /**
        Allows the user to download their reservation after they accept it
        First creates a csv row the parameters for the reservation as column values.

        AI was used to generate the rest (i.e. saving the .csv to the users phone)
     **/
    fun downloadUserOffer(context: Context, deviceId: String, reservation: Reservation): Uri? {
        val csv = "campsite,location,entrant,start_date, end_date,status\n" +
                "${reservation.campsite.name},${reservation.campsite.location}," +
                "${reservation.entrantId},${reservation.startDate} , ${reservation.endDate},${reservation.status}"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "reservation_confirmation.csv")
                put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            }
            val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values) ?: return null
            context.contentResolver.openOutputStream(uri)?.use { it.write(csv.toByteArray()) }
            return uri
        } else {
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: return null
            val file = File(dir, "reservation_confirmation.csv")
            file.writeText(csv)
            return Uri.fromFile(file)
        }
    }
}