package com.app.tiketin.v1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
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

        // Intent to open MainActivity when notification is clicked
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 
            0, 
            contentIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Ticket Booking",
                NotificationManager.IMPORTANCE_HIGH // Change to HIGH for heads-up and prominence
            ).apply {
                description = "Notification for successful ticket booking"
                enableLights(true)
                lightColor = android.graphics.Color.BLUE
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Pemesanan Berhasil!")
            .setContentText("Tiket untuk $wisataName ($packageName) telah dipesan.")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Pesanan tiket Anda untuk $wisataName dengan $packageName telah berhasil diproses. Silakan cek riwayat pesanan Anda."))
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set to HIGH
            .setContentIntent(pendingIntent) // Open app on click
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(notificationId, builder.build())
    }
}
