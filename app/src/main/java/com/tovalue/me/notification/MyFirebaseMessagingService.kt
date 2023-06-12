package com.tovalue.me.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tovalue.me.R
import com.tovalue.me.helper.MomensityBingoApp
import com.tovalue.me.ui.dashboard.DashboardActivity
import com.tovalue.me.util.Constants.DEVICE_TOKEN
import org.json.JSONException

class MyFirebaseMessagingService : FirebaseMessagingService() {
	var notificationId = 0
	
	override fun onNewToken(token: String) {
		try {
			Log.d("Firebase", "device_token: $token")
		} catch (exception: IllegalStateException) {
			Log.d("Firebase", "device_token: $exception")
		}
	}

	/**
	 * Called when message is received.
	 *
	 * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
	 */
	// [START receive_message]
	override fun onMessageReceived(remoteMessage: RemoteMessage) {
	
//		 There are two types of messages data messages and notification messages. Data messages are handled
//		 here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
//		 traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
//		 is in the foreground. When the app is in the background an automatically generated notification is displayed.
//		 When the user taps on the notification they are returned to the app. Messages containing both notification
//		 and data payloads are treated as notification messages. The Firebase console always sends notification
//		 messages.

		// TODO(developer): Handle FCM messages here.
		// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
		Log.d("fcm", "From: " + remoteMessage.from)
		Log.d("fcm", "Data: " + remoteMessage.notification)
		
		try {
			val messageData = remoteMessage.notification?.body.toString()
			sendNotification(messageData)
		} catch (e: JSONException) {
			e.printStackTrace()
		} finally {
			stopSelf()
		}

		// Also if you intend on generating your own notifications as a result of a received FCM
		// message, here is where that should be initiated. See sendNotification method below.
	}

	/**
	 * Create and show a simple notification containing the received FCM message.
	 *
	 * @param messageBody FCM message body received.
	 */
	private fun sendNotification(message: String) {
		val intent = Intent(this, DashboardActivity::class.java)
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
		val pendingIntent = PendingIntent.getActivity(this, 0, intent,
			PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
		
		val channelId = getString(R.string.default_notification_channel_id)
		val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
		val notificationBuilder: NotificationCompat.Builder =
			NotificationCompat.Builder(this, channelId)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(getString(R.string.app_name))
				.setContentText(message)
				.setAutoCancel(true)
				.setSound(defaultSoundUri)
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setContentIntent(pendingIntent)
		val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

		// Since android Oreo notification channel is needed.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(channelId,
				applicationContext.resources.getString(R.string.app_name),
				NotificationManager.IMPORTANCE_HIGH)
			channel.enableLights(true)
			channel.lightColor = Color.YELLOW
			channel.enableVibration(true)
			notificationManager.createNotificationChannel(channel)
		}
		notificationManager.notify(notificationId++ /* ID of notification */,
			notificationBuilder.build())
	}
}
