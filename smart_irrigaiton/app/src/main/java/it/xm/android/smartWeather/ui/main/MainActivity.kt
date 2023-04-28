package it.xm.android.smartWeather.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import it.xm.android.smartWeather.OpenWeatherMapService
import it.xm.android.smartWeather.R
import it.xm.android.smartWeather.databinding.MainActivityBinding
import it.xm.android.smartWeather.mainMenu
import it.xm.android.smartWeather.zoneSetUp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(){
    lateinit var bottomNav : BottomNavigationView

    private lateinit var binding: MainActivityBinding


    private lateinit var textView: TextView
    private val apiKey = "7d85604d75067f7a0da53ac8f70c5364"
    private val service = OpenWeatherMapService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)




        textView = findViewById(R.id.textView)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val forecast = withContext(Dispatchers.IO) {
                    service.getWeatherForecast("Bryan", "7d85604d75067f7a0da53ac8f70c5364")
                }

                val forecastString = StringBuilder()
                for (item in forecast.list) {
                    val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(item.dt_txt)!!
                    val dayOfWeek = SimpleDateFormat("EEEE", Locale.US).format(time)
                    forecastString.append("$dayOfWeek: ${item.main.temp}°C\n")
                }

                textView.text = forecastString.toString()
            } catch (e: Exception) {
                textView.text = "Error: ${e.message}"
                Log.e("MainActivity", "Exception: ${e.message}")
            }
        }




        bottomNav = findViewById(R.id.bottom_nav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home ->{
                    val intent = Intent(this, mainMenu::class.java)
                    startActivity(intent)
                    true
                }
                R.id.forcast ->{
                    val intent = Intent(this, MainActivity::class.java)
                    //startActivity(intent)
                    true
                }
                R.id.zones ->{
                    val intent = Intent(this, zoneSetUp::class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    false
                }
            }
        }

    }
}