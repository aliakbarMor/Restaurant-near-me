package mor.aliakbar.restaurantnearme.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import mor.aliakbar.restaurantnearme.ui.MainActivity
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant

class AppNotification private constructor(private val context: Context) {

    lateinit var notificationManager: NotificationManager
    lateinit var builder: NotificationCompat.Builder

    companion object {

        private var INSTANCE: AppNotification? = null
        fun getInstance(context: Context): AppNotification? {
            synchronized(AppNotification::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = AppNotification(context)
                }
                return INSTANCE
            }
        }
    }

    fun showNotification(rest: Restaurant, mealsTime: String?) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel: NotificationChannel

        val soundUri = Uri.parse(
            "android.resource://"
                    + context.packageName + "/" + R.raw.approach_the_restaurant
        )
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(context, soundUri)
        mediaPlayer.prepare()
        mediaPlayer.start()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel("app_id", "app", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        builder = NotificationCompat.Builder(context, "app_id")

        val contentText = if (mealsTime == null) {
            "به رستوران " + rest.name + " نزدیک میشوید"
        } else {
            "رستوران " + rest.name + " برای " + mealsTime + " اماده پذیرایی از شماست "
        }

        val bundle = Bundle()
        bundle.putParcelable("restaurant", rest)

        val mainPendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.restDetailFragment)
            .setArguments(bundle)
            .createPendingIntent()

        builder
            .setContentTitle(rest.name)
            .setContentText(contentText)
            .setContentIntent(mainPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_rest)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setSound(null)

        notificationManager.notify(1929, builder.build())
    }

}