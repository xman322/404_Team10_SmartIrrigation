package it.xm.android.smartWeather

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String,
    ): WeatherForecast
}
