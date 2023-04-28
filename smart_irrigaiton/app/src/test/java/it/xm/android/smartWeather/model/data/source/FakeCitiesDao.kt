package it.xm.android.smartWeather.model.data.source

import it.xm.android.smartWeather.infrastructure.room.daos.CitiesDao
import it.xm.android.smartWeather.infrastructure.room.entities.CityEntity

class FakeCitiesDao(var cities: MutableList<CityEntity> = mutableListOf()): CitiesDao {
    override suspend fun findCitiesStartWith(
        startName: String,
        numbOfResult: Int
    ): List<CityEntity> {
        return cities.take(3)
    }

    override suspend fun findCityByCoords(latitude: Float, longitude: Float): CityEntity? {
        return if (cities.isEmpty()) {
            null
        } else {
            cities.find { it.latitude == latitude && it.longitude == longitude }
        }
    }

    override suspend fun insertCities(vararg city: CityEntity): List<Long> {
        return if (cities.isEmpty()) {
            emptyList()
        } else {
            listOf(1, 2, 3, 4)
        }
    }

    override suspend fun deleteCityByCoords(vararg city: CityEntity) {
        TODO("Not yet implemented")
    }
}