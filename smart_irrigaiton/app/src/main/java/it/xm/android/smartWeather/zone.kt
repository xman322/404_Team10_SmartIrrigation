package it.xm.android.smartWeather


var zoneList = mutableListOf<zone>()

val ZONE_EXTRA = "zoneExtra"

class zone (
    var cover: Int,
    var zoneNumber: String,
    var enabled: String,
    var options: String,
    var grassType: String,
    var time: String,
    var waterDesired: Double,
    val id: Int? = zoneList.size
)


