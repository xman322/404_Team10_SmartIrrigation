package it.xm.android.smartWeather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenWeatherMapService {
    private val api = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenWeatherMapApi::class.java)

    suspend fun getWeatherForecast(city: String, apiKey: String): WeatherForecast {
        return api.getWeatherForecast(city, "metric", apiKey)
    }
}
