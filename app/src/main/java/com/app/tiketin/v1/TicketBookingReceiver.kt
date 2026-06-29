package com.app.tiketin.v1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class TicketBookingReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val wisataName = intent.getStringExtra("WISATA_NAME") ?: "Wisata"
        val packageName = intent.getStringExtra("PACKAGE_NAME") ?: "Paket"

        showNotification(context, wisataName, packageName)
    }

    private fun showNotification(context: Context, wisataName: String, packageName: String) {
        val channelId = "ticket_booking_channel"
        val notificationId = System.currentTimeMillis().toInt()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Ticket Booking",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notification for successful ticket booking"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Pemesanan Berhasil!")
            .setContentText("Tiket untuk $wisataName ($packageName) telah dipesan.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}
