package it.xm.android.smartWeather

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import it.xm.android.smartWeather.ui.main.MainActivity
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class mainMenu : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    private val networkChangeReceiver = NetworkChangeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)




        val tvMode = findViewById<TextView>(R.id.tvMode)


        val smart = findViewById<Button>(R.id.btnSmart)
        smart.setOnClickListener {
            tvMode.text = "Mode: Smart"
        }

        val simple = findViewById<Button>(R.id.btnSimple)
        simple.setOnClickListener {
            tvMode.text = "Mode: Simple"
            val intent = Intent(this, simpleMode::class.java)
            startActivity(intent)
        }
        val manual = findViewById<Button>(R.id.btnManual)
        manual.setOnClickListener {
            tvMode.text = "Mode: Manual"
            val intent = Intent(this, manualMode::class.java)
            startActivity(intent)
        }


        bottomNav = findViewById(R.id.bottom_nav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home ->{
                    val intent = Intent(this, mainMenu::class.java)
                    //startActivity(intent)
                    true
                }
                R.id.forcast ->{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.zones ->{
                    val intent = Intent(this, zoneSetUp::class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    true
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Stop the TimeCheckService when the activity is destroyed
        val intent = Intent(this, TimeCheckService::class.java)
        stopService(intent)
    }



}




fun scheduleNotification(context: Context, desiredTime: String) {
    // Define a notification ID to uniquely identify this notification.
    val notificationId = 12345

    // Create an intent that will be fired when the notification is clicked.
    val intent = Intent(context, mainMenu::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

    // Create a notification builder.
    val builder = NotificationCompat.Builder(context, "channel_id")
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Smart Irrigation")
        .setContentText("Sprinklers will now turn on")
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // Get the Central Standard Time zone.
    val timeZone = TimeZone.getTimeZone("America/Chicago")

    // Create a date formatter that will parse the desired time from the user.
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    dateFormat.timeZone = timeZone

    // Schedule a background thread to check the time every minute.
    thread {
        while (true) {
            // Get the current time in the Central Standard Time zone.
            val currentTime = Calendar.getInstance(timeZone)

            // Parse the desired time from the user and set it in the calendar object.
            val desiredTimeCalendar = Calendar.getInstance(timeZone)
            desiredTimeCalendar.time = dateFormat.parse(desiredTime)!!

            // Compare the current time with the desired time.
            if (currentTime.get(Calendar.HOUR_OF_DAY) == desiredTimeCalendar.get(Calendar.HOUR_OF_DAY)
                && currentTime.get(Calendar.MINUTE) == desiredTimeCalendar.get(Calendar.MINUTE)
            ) {
                // If the times match, send the notification.
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(notificationId, builder.build())

                // Exit the background thread.
                return@thread
            }

            // Wait for a minute before checking again.
            Thread.sleep(60000)
        }
    }
}