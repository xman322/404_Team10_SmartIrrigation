package it.xm.android.smartWeather

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import it.xm.android.smartWeather.databinding.ActivityZoneSetUpBinding
import it.xm.android.smartWeather.databinding.ActivityZonesBinding
import it.xm.android.smartWeather.ui.main.MainActivity

var count = 0
class zoneSetUp : AppCompatActivity(), ZoneClickListener {
    private lateinit var binding: ActivityZoneSetUpBinding
    private lateinit var database: DatabaseReference
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

        binding.update.setOnClickListener { setData() }







    }

    private fun setData() {
        var desired: Double = 0.0
        var desired2: Double = 0.0
        var desired3: Double = 0.0
        var desired4: Double = 0.0
        var desired5: Double = 0.0
        var desired6: Double = 0.0
        var desired7: Double = 0.0
        var desired8: Double = 0.0
        var desired9: Double = 0.0
        var en1: Boolean = true
        var en2: Boolean = true
        var en3: Boolean = true
        var en4: Boolean = true
        var en5: Boolean = true
        var en6: Boolean = true
        var en7: Boolean = true
        var en8: Boolean = true
        var en9: Boolean = true
        try {
            desired = zoneList[0].waterDesired
            desired2 = zoneList[1].waterDesired
            desired3 = zoneList[2].waterDesired
            desired4 = zoneList[3].waterDesired
            desired5 = zoneList[4].waterDesired
            desired6 = zoneList[5].waterDesired
            desired7 = zoneList[6].waterDesired
            desired8 = zoneList[7].waterDesired
            desired9 = zoneList[8].waterDesired
            en1 = zoneList[0].enabled
            en2 = zoneList[1].enabled
            en3 = zoneList[2].enabled
            en4 = zoneList[3].enabled
            en5 = zoneList[4].enabled
            en6 = zoneList[5].enabled
            en7 = zoneList[6].enabled
            en8 = zoneList[7].enabled
            en9 = zoneList[8].enabled
        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        database = FirebaseDatabase.getInstance().getReference("Zone1")
        database.child("Desired").setValue(desired).addOnSuccessListener {
            Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to update: Try again later", Toast.LENGTH_SHORT).show()
        }

        database = FirebaseDatabase.getInstance().getReference("Zone1")
        database.child("En").setValue(en1)




        database = FirebaseDatabase.getInstance().getReference("Zone2")
        database.child("Desired").setValue(desired2)

        database = FirebaseDatabase.getInstance().getReference("Zone2")
        database.child("En").setValue(en2)



        database = FirebaseDatabase.getInstance().getReference("Zone3")
        database.child("Desired").setValue(desired3)

        database = FirebaseDatabase.getInstance().getReference("Zone3")
        database.child("En").setValue(en3)



        database = FirebaseDatabase.getInstance().getReference("Zone4")
        database.child("Desired").setValue(desired4)

        database = FirebaseDatabase.getInstance().getReference("Zone4")
        database.child("En").setValue(en4)



        database = FirebaseDatabase.getInstance().getReference("Zone5")
        database.child("Desired").setValue(desired5)

        database = FirebaseDatabase.getInstance().getReference("Zone5")
        database.child("En").setValue(en5)



        database = FirebaseDatabase.getInstance().getReference("Zone6")
        database.child("Desired").setValue(desired6)

        database = FirebaseDatabase.getInstance().getReference("Zone6")
        database.child("En").setValue(en6)


        database = FirebaseDatabase.getInstance().getReference("Zone7")
        database.child("Desired").setValue(desired7)

        database = FirebaseDatabase.getInstance().getReference("Zone7")
        database.child("En").setValue(en7)



        database = FirebaseDatabase.getInstance().getReference("Zone8")
        database.child("Desired").setValue(desired8)

        database = FirebaseDatabase.getInstance().getReference("Zone8")
        database.child("En").setValue(en8)



        database = FirebaseDatabase.getInstance().getReference("Zone9")
        database.child("Desired").setValue(desired9)

        database = FirebaseDatabase.getInstance().getReference("Zone9")
        database.child("En").setValue(en9)




        var update: Int = 1
        database = FirebaseDatabase.getInstance().getReference("Update")
        database.setValue(update)




    }

    private fun readData() {
        database = FirebaseDatabase.getInstance().getReference("Zone1")
        database.child("Desired").get().addOnSuccessListener {
            if(it.exists()){
                val capacity:Float = it.value.toString().toFloat()
               // binding.textdata.setText(capacity.toString())
            }else{
                Toast.makeText(this, "Path doesnt exits", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "FAILED", Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateZones() {
        val zone1 = zone(
            R.drawable.grass,
            "Zone 1",
            true,
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone1)

        val zone2 = zone(
            R.drawable.grass,
            "Zone 2",
            true,
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone2)

        val zone3 = zone(
            R.drawable.grass,
            "Zone 3",
            true,
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone3)

        val zone4 = zone(
            R.drawable.grass,
            "Zone 4",
            true,
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone4)


        val zone5 = zone(
            R.drawable.grass,
            "Zone 5",
            true,
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone5)


        val zone6 = zone(
            R.drawable.grass,
            "Zone 6",
            true,
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone6)


        val zone7 = zone(
            R.drawable.grass,
            "Zone 7",
            true,
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone7)

        val zone8 = zone(
            R.drawable.grass,
            "Zone 8",
            true,
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone8)

        val zone9 = zone(
            R.drawable.grass,
            "Zone 9",
            true,
            "Lots of Sun",
            "Sand",
            "12:00",
            1.0
        )
        zoneList.add(zone9)


    }

    override fun onClick(zone: zone) {
        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra(ZONE_EXTRA, zone.id)
        startActivity(intent)
    }
}



