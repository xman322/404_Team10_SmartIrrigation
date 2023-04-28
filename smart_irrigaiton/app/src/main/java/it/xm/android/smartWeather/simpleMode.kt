package it.xm.android.smartWeather

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class simpleMode : AppCompatActivity() {

    private lateinit var timePickerButton: Button
    private lateinit var selectedTimeTextView: TextView
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_mode)

        val back = findViewById<Button>(R.id.btnBack)
        back.setOnClickListener {
            val intent = Intent(this, mainMenu::class.java)
            startActivity(intent)
        }


        timePickerButton = findViewById(R.id.timePickerButton)
        selectedTimeTextView = findViewById(R.id.timeTextView)

        // Set a click listener for the button
        timePickerButton.setOnClickListener {
            // Get the current time
            val currentTime = Calendar.getInstance()

            // Create a time picker dialog
            val timePickerDialog = TimePickerDialog(
                this,
                { _: TimePicker, hourOfDay: Int, minute: Int ->
                    // Save the selected time as a string
                    val selectedTimeCalendar = Calendar.getInstance()
                    selectedTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTimeCalendar.set(Calendar.MINUTE, minute)

                    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    selectedTime = dateFormat.format(selectedTimeCalendar.time)
                    //enter zone time here

                    for( i in 0 until zoneList.size){
                        zoneList[i].time = selectedTime
                    }

                    // Update the selected time text view
                    selectedTimeTextView.text = "Time selected: $selectedTime"
                },
                currentTime.get(Calendar.HOUR_OF_DAY),
                currentTime.get(Calendar.MINUTE),
                true
            )

            // Show the time picker dialog
            timePickerDialog.show()
        }


    }

    override fun onResume() {
        super.onResume()

        // If the selectedTime variable has a value, set it as the text of the button
        if (selectedTime.isNotEmpty()) {
            selectedTimeTextView.text = "Time selected: $selectedTime"
        }
    }

    override fun onPause() {
        super.onPause()

        // Save the selected time in shared preferences so it can be restored when the activity is resumed
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("selectedTime", selectedTime)
            apply()
        }
    }

    override fun onStop() {
        super.onStop()

        // Save the selected time in shared preferences so it can be restored when the activity is resumed
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("selectedTime", selectedTime)
            apply()
        }
    }

    override fun onStart() {
        super.onStart()

        // Restore the selected time from shared preferences if it exists
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        selectedTime = sharedPref.getString("selectedTime", "") ?: ""

        // If the selectedTime variable has a value, set it as the text of the button
        if (selectedTime.isNotEmpty()) {
            selectedTimeTextView.text = "Time selected: $selectedTime"
        }
    }


}





