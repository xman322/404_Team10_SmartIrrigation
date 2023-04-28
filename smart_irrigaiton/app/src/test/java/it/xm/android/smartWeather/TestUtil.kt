package it.xm.android.smartWeather

import it.xm.android.smartWeather.infrastructure.room.entities.CityEntity
import it.xm.android.smartWeather.model.data.*

class TestUtil {

    fun mockWeatherData(timeInMillis: Long): ListData {
        return ListData(
            timeInMillis,
            listOf(Weather("icon")),
            Main(23.4f)
        )
    }

    fun mockCityData(): CityData {
        return CityData(
            Country("UK"),
            listOf("region"),
            Geoloc(12.3456f, 12.3456f),
            LocaleNames(listOf("London"))
        )
    }

    fun mockCityEntity(insertTimeInMillis: Long): CityEntity {
        return CityEntity(
            "Lisbona",
            "Portogallo",
            0,
            "admin code",
            12.3456f,
            12.3456f,
            insertTimeInMillis
        )
    }
}