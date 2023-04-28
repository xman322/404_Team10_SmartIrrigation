package it.xm.android.smartWeather

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import it.xm.android.smartWeather.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var preferences: SharedPreferences
    private lateinit var preferences2: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val zoneID = intent.getIntExtra(ZONE_EXTRA, -1)
        val zone = zoneFromID(zoneID)
        if(zone != null){
            binding.zone.setImageResource(zone.cover)
            binding.zonenumber.text = zone.zoneNumber
            binding.enabled.text = zone.enabled
            binding.options.text = zone.options
            binding.grassType.text = zone.grassType
            binding.simpleTime.text = zone.time

        }


        // code to send notification
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "Channel Name",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel Description"
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        if (zone != null) {
            scheduleNotification(this, zone.time)
        }*/






        val button = findViewById<Button>(R.id.btnEnable)
        button.setOnClickListener {
            if (zone != null) {
                if(zone.enabled == "Enabled"){
                    zone.enabled = "Disabled"

                }
                else{
                    zone.enabled = "Enabled"
                }
                finish()
                overridePendingTransition(0, 0)
                startActivity(getIntent())
                overridePendingTransition(0, 0)
                button.setText("Enable")
            }
        }

        if (zone != null) {
            if(zone.enabled == "Disabled"){
                button.setText("Enable")
            }
            else if(zone.enabled == "Enabled"){
                button.setText("Disable")
            }
        }
        val enabled = findViewById<TextView>(R.id.enabled)
        if (zone != null) {
            enabled.setText(zone.enabled)

        }
        val back = findViewById<Button>(R.id.btnBack)
        back.setOnClickListener {
            val intent = Intent(this, zoneSetUp::class.java)
            startActivity(intent)
        }




        preferences = getPreferences(Context.MODE_PRIVATE)
        val soils = arrayOf("Sand", "Loamy Sand", "Sandy Loam", "Loam", "Clay Loam", "Silty Clay", "Clay")

        val spinner = findViewById<Spinner>(R.id.spSoil)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, soils)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val savedValue = preferences.getString("selected_soil", "")
        if(savedValue != null && savedValue.isNotEmpty()){
            val index = soils.indexOf(savedValue)
            spinner.setSelection(index)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedSoil = p0?.getItemAtPosition(p2).toString()
                if (zone != null) {
                    zone.grassType = selectedSoil

                    preferences.edit().putString("selected_soil", selectedSoil).apply()
                    if(selectedSoil == "Sand"){
                        zone.waterDesired = 1.5
                    }
                    else if(selectedSoil == "Loamy Sand"){
                        zone.waterDesired = 1.5
                    }
                    else if(selectedSoil == "Sandy Loam"){
                        zone.waterDesired = 1.5
                    }
                    else if(selectedSoil == "Loam"){
                        zone.waterDesired = 1.5
                    }
                    else if (selectedSoil == "Clay Loam"){
                        zone.waterDesired = 1.25
                    }
                    else if(selectedSoil == "Silty Clay"){
                        zone.waterDesired = 1.0
                    }
                    else if(selectedSoil == "Clay"){
                        zone.waterDesired = 0.75
                    }

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


        val spinner2 = findViewById<Spinner>(R.id.spShade)
        preferences2=getPreferences(Context.MODE_PRIVATE)
        val shades = arrayOf("Lots of Sun", "Some Shade", "Lots of Shade", "Mostly Shade")
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, shades)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2
        val savedValue2 = preferences2.getString("selected_shade", "")
        if(savedValue2 != null && savedValue2.isNotEmpty()){
            val index2 = shades.indexOf(savedValue2)
            spinner2.setSelection(index2)
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedShade = p0?.getItemAtPosition(p2).toString()
                if (zone != null) {
                    zone.options = selectedShade

                    if(selectedShade == "Lots of Sun"){
                        zone.waterDesired += (zone.waterDesired * 0.5)
                    }
                    else if(selectedShade == "Some Shade"){
                        zone.waterDesired += (zone.waterDesired * 0.25)
                    }
                    else if (selectedShade == "Mostly Shade"){
                        zone.waterDesired -= (zone.waterDesired * 0.25)
                    }

                    preferences2.edit().putString("selected_shade", selectedShade).apply()

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }


    }



    private fun zoneFromID(zoneID: Int): zone? {
        for (zone in zoneList){
            if(zone.id == zoneID)
                return zone
        }
        return null
    }

    private fun enabledDisabled() {

    }
}