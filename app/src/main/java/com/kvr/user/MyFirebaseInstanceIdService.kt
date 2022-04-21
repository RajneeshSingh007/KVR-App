package com.kvr.user


import android.app.Notification
import android.app.NotificationChannel
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.google.firebase.messaging.RemoteMessage
import com.kvr.user.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class MyFirebaseInstanceIdService : FirebaseMessagingService() {

    val TAG = "MyFirebaseInstanceIdService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if(token.isNotEmpty()){
            Log.e(TAG, token)
            //send token
            val tokenIntent = Intent()
            tokenIntent.action = "FirebaseToken"
            tokenIntent.putExtra("token", token)
            sendBroadcast(tokenIntent)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(remoteMessage)
    }



    /**
     * notification data
     */
    private fun sendNotification(remoteMessage: RemoteMessage) {
        val channelId = getString(R.string.app_name)+"Notification"
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Create the pending intent to launch the activity
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this, 155, intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this, 155, intent,
                PendingIntent.FLAG_ONE_SHOT
            )
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,getString(R.string.app_name),NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(5855, notificationBuilder.build())
    }


}