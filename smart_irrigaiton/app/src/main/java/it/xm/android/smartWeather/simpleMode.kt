package it.xm.android.smartWeather

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class simpleMode : AppCompatActivity() {

    private lateinit var timePickerButton: Button
    private lateinit var selectedTimeTextView: TextView
    private var selectedTime: String = ""
    private var selDay: Int = 0
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_mode)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

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
                    var first: Char = selectedTime.get(0)
                    var second: Char = selectedTime.get(1)
                    var secondlast: Char = selectedTime.get(3)
                    var last: Char = selectedTime.get(4)

                    var time1: String = first.toString() + second.toString()
                    var time1int: Int = time1.toInt()

                    var time2: String = secondlast.toString() + last.toString()
                    var time2Int: Int = time2.toInt()

                    var FINALTIME: Int = time1int*60 + time2Int
                    database = FirebaseDatabase.getInstance().getReference("SimpleTime")
                    if(selDay == 1){
                        database.setValue(-1)
                    }
                    else{
                        database.setValue(FINALTIME)
                    }


                    var update: Int = 1
                    database = FirebaseDatabase.getInstance().getReference("Update")
                    database.setValue(update)
                },
                currentTime.get(Calendar.HOUR_OF_DAY),
                currentTime.get(Calendar.MINUTE),
                true
            )

            // Show the time picker dialog
            timePickerDialog.show()
        }

/*
        val daysOfWeek = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

        for (day in daysOfWeek) {
            val buttonId = resources.getIdentifier("button$day", "id", packageName)
            val button = findViewById<Button>(buttonId)

            button.setOnClickListener {
                // Change the background color of the clicked button to dark
                button.setBackgroundResource(android.R.color.darker_gray)
            }
        }

*/

        // Reference to your buttons
        val buttonSunday = findViewById<Button>(R.id.buttonSunday)
        val buttonMonday = findViewById<Button>(R.id.buttonMonday)
        val buttonTuesday = findViewById<Button>(R.id.buttonTuesday)
        val buttonWednesday = findViewById<Button>(R.id.buttonWednesday)
        val buttonThursday = findViewById<Button>(R.id.buttonThursday)
        val buttonFriday = findViewById<Button>(R.id.buttonFriday)
        val buttonSaturday = findViewById<Button>(R.id.buttonSaturday)

        // Restore button states
        restoreButtonState(buttonSunday, "buttonSunday")
        restoreButtonState(buttonMonday, "buttonMonday")
        restoreButtonState(buttonTuesday, "buttonTuesday")
        restoreButtonState(buttonWednesday, "buttonWednesday")
        restoreButtonState(buttonThursday, "buttonThursday")
        restoreButtonState(buttonFriday, "buttonFriday")
        restoreButtonState(buttonSaturday, "buttonSaturday")

        // Set click listeners
        setClickListener(buttonSunday, "buttonSunday")
        setClickListener(buttonMonday, "buttonMonday")
        setClickListener(buttonTuesday, "buttonTuesday")
        setClickListener(buttonWednesday, "buttonWednesday")
        setClickListener(buttonThursday, "buttonThursday")
        setClickListener(buttonFriday, "buttonFriday")
        setClickListener(buttonSaturday, "buttonSaturday")

    }


    private fun setClickListener(button: Button, key: String) {
        button.setOnClickListener {
            // Toggle the button state
            val isPressed = !button.isSelected
            button.isSelected = isPressed

            // Save the button state
            saveButtonState(key, isPressed)

            // Change button color based on state
            val colorResId = if (isPressed) android.R.color.darker_gray else R.color.light_grey
            val color = ContextCompat.getColor(this, colorResId)
            button.setBackgroundColor(color)
            if((key == "buttonThursday") && (isPressed)){
                selDay = 1
            }
            else{
                selDay = 0
            }
        }
    }

    private fun saveButtonState(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }


    private fun restoreButtonState(button: Button, key: String) {
        val isPressed = sharedPreferences.getBoolean(key, false)
        button.isSelected = isPressed

        val colorResId = if (isPressed) android.R.color.darker_gray else R.color.light_grey
        val color = ContextCompat.getColor(this, colorResId)
        button.setBackgroundColor(color)
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




