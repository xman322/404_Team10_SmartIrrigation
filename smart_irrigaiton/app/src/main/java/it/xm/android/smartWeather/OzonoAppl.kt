package it.xm.android.smartWeather

import android.app.Application


class OzonoAppl: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: OzonoAppl
            private set
    }
}