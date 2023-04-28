package it.xm.android.smartWeather

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import it.xm.android.smartWeather.databinding.ActivityZoneSetUpBinding
import it.xm.android.smartWeather.databinding.ActivityZonesBinding
import it.xm.android.smartWeather.ui.main.MainActivity

var count = 0
class zoneSetUp : AppCompatActivity(), ZoneClickListener {
    private lateinit var binding: ActivityZoneSetUpBinding
    lateinit var bottomNav : BottomNavigationView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZoneSetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if(count == 0){
            populateZones()
        }
        count +=1

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


        scheduleNotification(this, zoneList[0].time)



        val zoneSetUp = this
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(applicationContext, 3)
            adapter = CardAdapter(zoneList, zoneSetUp )
        }

        bottomNav = findViewById(R.id.bottom_nav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    val intent = Intent(this, mainMenu::class.java)
                    startActivity(intent)
                    true
                }
                R.id.forcast -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.zones -> {
                    val intent = Intent(this, zoneSetUp::class.java)
                    //startActivity(intent)
                    true
                }
                else -> {
                    false
                }
            }
        }







    }

    private fun populateZones() {
        val zone1 = zone(
            R.drawable.grass,
            "Zone 1",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone1)

        val zone2 = zone(
            R.drawable.grass,
            "Zone 2",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone2)

        val zone3 = zone(
            R.drawable.grass,
            "Zone 3",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone3)

        val zone4 = zone(
            R.drawable.grass,
            "Zone 4",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone4)


        val zone5 = zone(
            R.drawable.grass,
            "Zone 5",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone5)


        val zone6 = zone(
            R.drawable.grass,
            "Zone 6",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone6)


        val zone7 = zone(
            R.drawable.grass,
            "Zone 7",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone7)

        val zone8 = zone(
            R.drawable.grass,
            "Zone 8",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone8)

        val zone9 = zone(
            R.drawable.grass,
            "Zone 9",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone9)


        val zone10 = zone(
            R.drawable.grass,
            "Zone 10",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone10)

        val zone11 = zone(
            R.drawable.grass,
            "Zone 11",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone11)

        val zone12 = zone(
            R.drawable.grass,
            "Zone 12",
            "Enabled",
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone12)

    }

    override fun onClick(zone: zone) {
        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra(ZONE_EXTRA, zone.id)
        startActivity(intent)
    }
}