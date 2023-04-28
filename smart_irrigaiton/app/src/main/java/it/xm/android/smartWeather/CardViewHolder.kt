package it.xm.android.smartWeather

import androidx.recyclerview.widget.RecyclerView
import it.xm.android.smartWeather.databinding.CardCallBinding

class CardViewHolder(
    private val cardCallBinding: CardCallBinding,
    private val clickListener: ZoneClickListener
) : RecyclerView.ViewHolder(cardCallBinding.root)
{
    fun bindZone(zon: zone){
        cardCallBinding.zone.setImageResource(zon.cover)
        cardCallBinding.zonenumber.text = zon.zoneNumber
        cardCallBinding.enabled.text = zon.enabled


        cardCallBinding.cardView.setOnClickListener{
            clickListener.onClick(zon)
        }
    }


}