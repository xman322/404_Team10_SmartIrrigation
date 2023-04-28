package it.xm.android.smartWeather

data class WeatherForecast(
    val list: List<Forecast>,
) {
    data class Forecast(
        val dt_txt: String,
        val main: Main,
        val weather: List<Weather>,
    ) {
        data class Main(
            val temp: Double,
        )

        data class Weather(
            val main: String,
            val description: String,
            val icon: String,
        )
    }
}

