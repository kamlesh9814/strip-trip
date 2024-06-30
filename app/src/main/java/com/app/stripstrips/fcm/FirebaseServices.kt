package com.app.stripstrips.fcm
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.app.stripstrips.R
import com.app.stripstrips.activity.MainActivity
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.REQUEST_CODE
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseService : FirebaseMessagingService() {
    var TAG: String = "FirebaseService"

    var intent: Intent? = null
    var mNotificationModel: CustomNotification? = null

    var mTitle = ""
    var mentionMsg = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e(TAG, message.data.toString())
        mNotificationModel = Gson().fromJson(message.data["data"], CustomNotification::class.java)
        mentionMsg = message.data["body"].toString()
        mTitle = message.data["title"].toString()

        Log.e(TAG, "onMessageReceived: ${mTitle}")
        Log.e(TAG, "onMessageReceived: ${mNotificationModel}")
        sendNotification()
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun sendNotification() {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent: PendingIntent? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PendingIntent.getActivity(
                    application,
                    REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getActivity(
                    application,
                    REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }


        val pattern = longArrayOf(500, 500, 500, 500, 500)
        val channelId = getString(R.string.default_notification_channel_id)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle("HII")
            .setContentText("jjj")
            .setAutoCancel(true)
            .setVibrate(pattern)
            .setLights(Color.BLUE, 1, 1)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(
            1656,
            notificationBuilder.build()
        )

//         notificationManager.notify(1 /* ID of notification */, notificationBuilder.build())


    }

}