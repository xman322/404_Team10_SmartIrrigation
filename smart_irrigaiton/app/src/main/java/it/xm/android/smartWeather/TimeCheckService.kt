package it.xm.android.smartWeather

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.*
import androidx.core.app.NotificationCompat
import it.xm.android.smartWeather.ui.main.MainActivity
import java.text.SimpleDateFormat
import java.util.*



class TimeCheckService : Service() {

    companion object {
        const val NOTIFICATION_ID = 123
    }

    private var timer: CountDownTimer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val time = intent?.getStringExtra("time")

        // Check if the time is valid
        if (time == null || time.isEmpty()) {
            stopSelf()
            return super.onStartCommand(intent, flags, startId)
        }

        // Parse the time
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val desiredTime = formatter.parse(time)

        // Start a timer to check the time periodically
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val currentTime = Calendar.getInstance().time
                if (currentTime >= desiredTime) {
                    sendNotification()
                    stopSelf()
                }
            }

            override fun onFinish() {}
        }
        timer?.start()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun sendNotification() {
        val notificationIntent = Intent(this, mainMenu::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle("Times Match")
            .setContentText("The desired time matches the current time.")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}

