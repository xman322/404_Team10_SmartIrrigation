package it.xm.android.smartWeather

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import it.luccap11.android.smartWeather.databinding.CardCallBinding

class CardViewHolder(
    private val cardCallBinding: CardCallBinding,
    private val clickListener: ZoneClickListener
) : RecyclerView.ViewHolder(cardCallBinding.root)
{
    //@SuppressLint("ResourceAsColor")
    fun bindZone(zon: zone){
        cardCallBinding.zone.setImageResource(zon.cover)
        cardCallBinding.zonenumber.text = zon.zoneNumber
        val color1 = R.color.red
        val color2 = R.color.black

        if(zon.enabled == true){
            cardCallBinding.enabled.text = "Enabled"


        }
        else{
            cardCallBinding.enabled.text = "Disabled"

            cardCallBinding.cardView.setBackgroundColor(4289342499.toInt())
            cardCallBinding.zonenumber.setTextColor(4294967295.toInt())
            cardCallBinding.enabled.setTextColor(4294967295.toInt())

        }




        cardCallBinding.cardView.setOnClickListener{
            clickListener.onClick(zon)
        }
    }


}