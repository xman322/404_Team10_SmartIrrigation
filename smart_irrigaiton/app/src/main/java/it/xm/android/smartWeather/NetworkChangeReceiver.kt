package it.xm.android.smartWeather

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.app.NotificationCompat

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //var value: Int = 0
        if (isInternetConnected(context!!)) {
            // Internet is connected

            Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show()
        } else {
            // Internet is disconnected, send toast notification
            Toast.makeText(context, "Internet Disconnected: Please Wait", Toast.LENGTH_LONG).show()
            //value += 1
        }
    }

    private fun isInternetConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}

