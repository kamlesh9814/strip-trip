package com.app.stripstrips.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.app.stripstrips.R
import com.app.stripstrips.activity.MainActivity
import com.app.stripstrips.activity.TripsDetailsLocationActivity
import com.app.stripstrips.activity.TripsDiaryDetailsLocationActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson


class FcmService : FirebaseMessagingService() {
    private val TAG: String = javaClass.simpleName
    var mNotificationModel: CustomNotification? = null
    var message = ""
    var title = ""
    var tripId = ""
    var status = ""
    var id = ""

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "onMessageReceived" + remoteMessage.data.toString())
        Log.e(TAG, "onMessageReceivedNoty" + remoteMessage.notification.toString())
        message = remoteMessage.data["message"] ?: ""
        title = remoteMessage.data["title"] ?: ""
        tripId = remoteMessage.data["actionId"] ?: ""

        Log.e(TAG, "onMessageReceived" + tripId)

        // mNotificationModel = Gson().fromJson(remoteMessage.data.toString(), CustomNotification::class.java)
        Log.e(TAG, "onMessageReceived1111: ${mNotificationModel}")
//if(tripId!=null && tripId!=""){
        sendNotification(remoteMessage.data)}
//    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(data: Map<String, String>) {
        val notificationType = data["notificationType"]!!.toInt()
        message = data["message"] ?: ""
        title = data["title"] ?: ""

        val intent = Intent(this, TripsDetailsLocationActivity::class.java)
        intent.putExtra("Trip_Id",tripId)
        intent.putExtra("thumbImage", "")
        intent.putExtra("from","notification")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this, 0,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(
                this, 0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val channelId = "Default"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setChannelId(channelId)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}