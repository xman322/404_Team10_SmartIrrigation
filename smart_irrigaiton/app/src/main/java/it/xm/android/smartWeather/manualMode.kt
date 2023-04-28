package it.xm.android.smartWeather

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService





class manualMode : AppCompatActivity() {


    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private var timer: CountDownTimer? = null
    private val CHANNEL_ID = "Timer Notification Channel"
    private val NOTIFICATION_ID = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_mode)

        val back = findViewById<Button>(R.id.btnBack)
        back.setOnClickListener {
            val intent = Intent(this, mainMenu::class.java)
            startActivity(intent)
        }


        startButton = findViewById(R.id.start_button)
        stopButton = findViewById(R.id.stop_button)

        createNotificationChannel()

        startButton.setOnClickListener {
            timer = object: CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    // Timer is counting down (1 second intervals)
                }

                override fun onFinish() {
                    // Timer is finished
                    sendNotification()
                }
            }.start()
        }

        stopButton.setOnClickListener {
            timer?.cancel()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer Notification Channel"
            val descriptionText = "Channel for timer notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun sendNotification() {
        val intent = Intent(this, manualMode::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Smart Irrigation")
            .setContentText("Manual Mode has exceeded one hour limit")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }



}