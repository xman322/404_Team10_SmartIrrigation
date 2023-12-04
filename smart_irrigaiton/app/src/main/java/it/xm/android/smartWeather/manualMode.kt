package it.xm.android.smartWeather

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import it.xm.android.smartWeather.databinding.ActivityManualModeBinding



class manualMode : AppCompatActivity() {
    private lateinit var binding: ActivityManualModeBinding
    private lateinit var database: DatabaseReference

    private lateinit var sharedPreferences: SharedPreferences


    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private var timer: CountDownTimer? = null
    private val CHANNEL_ID = "Timer Notification Channel"
    private val NOTIFICATION_ID = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManualModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)



        val back = findViewById<Button>(R.id.btnBack)
        back.setOnClickListener {
            val intent = Intent(this, mainMenu::class.java)
            startActivity(intent)
        }


        setUpToggleButton(R.id.toggleButton1, "Zone1")
        setUpToggleButton(R.id.toggleButton2, "Zone2")
        setUpToggleButton(R.id.toggleButton3, "Zone3")
        setUpToggleButton(R.id.toggleButton4, "Zone4")
        setUpToggleButton(R.id.toggleButton5, "Zone5")
        setUpToggleButton(R.id.toggleButton6, "Zone6")
        setUpToggleButton(R.id.toggleButton7, "Zone7")
        setUpToggleButton(R.id.toggleButton8, "Zone8")
        setUpToggleButton(R.id.toggleButton9, "Zone9")



/*
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
        }*/
    }

/*
    private fun setUpToggleButton(toggleButtonId: Int, key: String) {
        val toggleButton = findViewById<ToggleButton>(toggleButtonId)

        // Set the initial state based on the saved value in SharedPreferences
        toggleButton.isChecked = sharedPreferences.getBoolean(key, false)

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            // Handle the state change here

            // Save the state in SharedPreferences
            sharedPreferences.edit().putBoolean(key, isChecked).apply()

            if (isChecked) {
                // Zone is on
                var mode: Int = 1
                database = FirebaseDatabase.getInstance().getReference(key)
                database.child("On").setValue(mode)
            } else {
                // Zone is off
                var mode: Int = 0
                database = FirebaseDatabase.getInstance().getReference(key)
                database.child("On").setValue(mode)
            }
        }
    }*/

    private fun setUpToggleButton(toggleButtonId: Int, key: String) {
        val toggleButton = findViewById<ToggleButton>(toggleButtonId)

        // Set the initial state based on the saved value in SharedPreferences
        toggleButton.isChecked = sharedPreferences.getBoolean(key, false)

        // Set the background color based on the initial state
        updateToggleButtonBackground(toggleButton, toggleButton.isChecked)

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            // Handle the state change here

            // Save the state in SharedPreferences
            sharedPreferences.edit().putBoolean(key, isChecked).apply()

            // Update the background color based on the state
            updateToggleButtonBackground(toggleButton, isChecked)

            if (isChecked) {
                // Zone is on
                var mode: Int = 1
                database = FirebaseDatabase.getInstance().getReference(key)
                database.child("On").setValue(mode)

                var update: Int = 1
                database = FirebaseDatabase.getInstance().getReference("Update")
                database.setValue(update)
            } else {
                // Zone is off
                var mode: Int = 0
                database = FirebaseDatabase.getInstance().getReference(key)
                database.child("On").setValue(mode)


                var update: Int = 1
                database = FirebaseDatabase.getInstance().getReference("Update")
                database.setValue(update)
            }
        }
    }

    private fun updateToggleButtonBackground(toggleButton: ToggleButton, isChecked: Boolean) {
        val colorResId = if (isChecked) {
            android.R.color.darker_gray
        } else {
            R.color.light_grey
        }

        toggleButton.setBackgroundResource(colorResId)
    }




    private fun readData() {
        database = FirebaseDatabase.getInstance().getReference("Zone1")
        database.child("Desired").get().addOnSuccessListener {
            if(it.exists()){
                val capacity:Float = it.value.toString().toFloat()
                //binding.textViewData.setText(capacity.toString())
            }else{
                Toast.makeText(this, "Path doesnt exits", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show()
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
