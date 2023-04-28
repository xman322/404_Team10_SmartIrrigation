package it.xm.android.smartWeather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import it.xm.android.smartWeather.ui.main.MainActivity

class zones : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zones)


        val calcButton = findViewById<Button>(R.id.btnEnter)

        calcButton.setOnClickListener {
            val intent = Intent(this, zoneSetUp::class.java)
            startActivity(intent)

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
                    val intent = Intent(this, zones::class.java)
                    //startActivity(intent)
                    true
                }
                else -> {
                    false
                }
            }
        }

    }
}
